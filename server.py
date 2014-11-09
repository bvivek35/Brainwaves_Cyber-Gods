#!/usr/bin/env python

import sqlite3
import pexpect


path_if_adb = "/home/vb-deb/inp.txt.ref"
path_of_adb = "/home/vb-deb/out.txt.ref"

adb_pull_cmd= "adb pull /sdcard/input " + path_if_adb
adb_push_cmd= "adb push " + path_of_adb +  " /sdcard/output "


def take_from_adb() :
	child = pexpect.spawn(adb_pull_cmd)
	child.expect(pexpect.EOF)

def give_to_adb() :
	child = pexpect.spawn(adb_pull_cmd)
	child.expect(pexpect.EOF)


def print_db():
	global cur
	rs = cur.execute("select * from acc_details")
	rs = rs.fetchall()
	print rs
	
def do_wd(rs) :
	global acc
	global imei
	global pin
	global cur
	rs = rs.fetchone()
	if rs == None :
		return ERROR_ACC
	acc_db = rs[0]
	imei_db = rs[1]
	if imei_db != imei :
		return ERROR_IMEI	
	pin_db = rs[2]
	if pin_db != pin :
		return ERROR_PIN
	bal_db = rs[3]
	if bal_db < bal :
		return ERROR_BAL
	
	bal_db = bal_db - bal
	cur.execute("update acc_details set bal="+str(bal_db)+" where acc like (\'"+str(acc)+"\')")
	rs = cur.execute("select * from transactions where acc like (\'" + str(acc) + "\')")
	rs = rs.fetchone()
	if rs == None :
		cur.execute(get_insert("transactions", acc, bal))
	cur.execute("update transactions set wd_amt="+str(bal)+" where acc like (\'"+str(acc)+"\')")
	return SUCCESS_WD + "ACC : " + str(acc) + "\tBAL REMAINING : "+ str(bal_db) + " LAST TRANSACTION : " + str(bal)
	
def get_mini_stmt() :
	global cur
	global acc
	rs = cur.execute("select * from acc_details where acc like (\'"+str(acc)+"\')")
	rs = rs.fetchone()
	
	if rs == None :
		return ERROR_ACC
	acc_db = rs[0]
	imei_db = rs[1]
	if imei_db != imei :
		return ERROR_IMEI	
	pin_db = rs[2]
	if pin_db != pin :
		return ERROR_PIN
	
	s = "ACC : " + rs[0] + "\tBALANCE : " + str(rs[3])
	rs = cur.execute("select * from transactions where acc like (\'" + str(acc) + "\')")
	rs = rs.fetchone()
	if rs == None :
		return "0,NO TRANSACTIONS DONE"	
	return "1,TRANSACTION DONE : \n" + s 

path_db = "/home/vb-deb/details.db"

path_if = "/home/vb-deb/inp.txt.ref"
path_of = "/home/vb-deb/out.txt.ref"

create_table_acc_details = "create table if not exists acc_details (acc text PRIMARY KEY, imei text, pin text, bal real)"

create_table_transactions = "create table if not exists transactions (acc text PRIMARY KEY, wd_amt real)"

def get_insert(table, *l) :
	s = "INSERT OR IGNORE INTO " + table + " values ("
	if table == "acc_details" :
		s = s + "\'"+str(l[0])+"\'" +" , "+ "\'"+str(l[1])+"\'" +" , "+ "\'"+str(l[2])+"\'" +" , "+ str(l[3])	
		s = s + ")"
	elif table == "transactions" :
		s = s + "\'"+str(l[0])+"\'" +" , "+ str(l[1]) 
		s = s + ")"
	return s



ERROR_ACC = "0,ACCOUNT NUMBER NOT REGISTERED\n"
ERROR_IMEI = "0,IMEI NUMBER NOT MATCHING\n"
ERROR_PIN = "0,WRONG PIN ENTERED\n"
ERROR_BAL = "0,YOU DONOT HAVE ENOUGH BALANCE\n"
SUCCESS_REG = "1,REGISTERED USER\n"
SUCCESS_WD = "1,WITHDRAWL SUCCESS.\n"

take_from_adb()

#read the request
inp = open(path_if, "r")
req = inp.read()
inp.close()


#create or open db
conn = sqlite3.connect(path_db)
cur = conn.cursor()

cur.execute(create_table_acc_details)
cur.execute(create_table_transactions)

#parse input
req = req.strip().split(",")
op = int(req[0])
acc = req[1]
imei = req[2]
pin = req[3]
bal = float(req[4])

out = open(path_of, "w")

if op == 1 :
	query = get_insert("acc_details", acc, imei, pin, bal)
	cur.execute(query)
	out.write("1,REGISTERED")	
	
elif op == 2 :
	query = "select * from acc_details where acc like (\'" + str(acc) + "\')"
	rs = cur.execute(query)
	s = do_wd(rs)
	out.write(s)
	
elif op == 3 :
	s = get_mini_stmt()
	out.write(s)	

out.write("\n")

out.close()

conn.commit()
cur.close()
conn.close()

give_to_adb()

