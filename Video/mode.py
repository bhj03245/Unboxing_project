import pymysql

mydb = pymysql.connect(
host='localhost',
user='pi',
passwd='myub',
database='ub_project'
)
mycursor = mydb.cursor()

sql = "select users_mode from users"
mycursor.execute(sql)
mode = mycursor.fetchall()[0][0]
print(mode)
mydb.close

