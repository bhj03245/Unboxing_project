#include <iostream>
#include <string>
#include <list>
#include <mysql.h>

#define DB_HOST "localhost"
#define DB_USER "root"
#define DB_PW "password"
#define DB_NAME "web"

using namespace std;

int main() {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 3306, NULL, 0);
	mysql_query(&mysql, "SELECT * FROM video");
	res = mysql_store_result(&mysql);
	fields = mysql_num_fields(res);

	while (row = mysql_fetch_row(res))
	{
		for (i = 0; i < fields; i++){
			cout << row[i]<< ' ';
			vlist.push_back(row[i]);
		}
		cout << endl;
	}
	cout << endl;

	for (iter = vlist.begin(); iter != vlist.end(); ++iter) {
		cout << *iter << ' ';
	}
	mysql_free_result(res);
	mysql_close(&mysql);
	return 0;
}