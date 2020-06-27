#include <sys/types.h>
#include <dirent.h>
#include <iostream>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <opencv4/opencv2/opencv.hpp>
#include <time.h>
#include <error.h>
#include <cstring>
#include "/usr/include/mariadb/mysql.h"

#define DB_HOST "localhost"
#define DB_USER "pi"
#define DB_PW "myub"
#define DB_NAME "ub_project"

using namespace std;
using namespace cv;

class Video {
private:
	int num;
	char fileName[25];
	int fileSize;
	char fileLength[10];
	char makeTime[30];
	char resolution[10];
	char url[100];

public:
	void setVideo(int _num, char* _fileName, int _fileSize, char* _fileLength, char* _makeTime, char* _resolution, char* _url)
	{
		this->num = _num;
		strcpy(this->fileName, _fileName);
		this->fileSize = _fileSize;
		strcpy(this->fileLength, _fileLength);
		strcpy(this->makeTime, _makeTime);
		strcpy(this->resolution, _resolution);
		strcpy(this->url, _url);
	}

	int getNum() {
		return this->num;
	}

	char* getName() {
		return this->fileName;
	}

	int getSize() {
		return this->fileSize;
	}

	char* getLength() {
		return this->fileLength;
	}

	char* getMakeTime() {
		return this->makeTime;
	}

	char* getResolution() {
		return this->resolution;
	}

	char* getUrl() {
		return this->url;
	}

	void printInfo() {
		cout << "--------------------------------------------" << endl;
		cout << "NUM : " << getNum() << endl;
		cout << "FileName : " << getName() << endl;
		cout << "FileSize : " << getSize() << " Byte" << endl;
		cout << "FileLength : " << getLength() << endl;
		cout << "MakeTime : " << getMakeTime() << endl;
		cout << "Resolution : " << getResolution() << endl;
		cout << "Path : " << getUrl() << endl;
		cout << "--------------------------------------------" << endl;
	}
};

void norm_db_insert(Video vid);
void impt_db_insert(Video vid);
void park_db_insert(Video vid);
void manl_db_insert(Video vid);
void db_select();
void db_update();
char* timeToString(struct tm* t);

void norm_db_insert(Video vid) {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;
	char norm_buf[500];
	char exist_query[100];
	int x;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	
	sprintf(norm_buf, "insert into norm (norm_num, norm_name, norm_size, norm_length, norm_mktime, norm_resolution, norm_url) values(%d,\'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\') ON DUPLICATE KEY UPDATE norm_name=\'%s\', norm_size=%d, norm_mktime=\'%s\'", vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl(), vid.getName(), vid.getSize(), vid.getMakeTime());
	
	cout << norm_buf << endl;
	if (mysql_query(&mysql, norm_buf) != 0) {
		cout << "Insert Error" << endl;
	}
	cout << "Insert Success!!" << endl;
	mysql_close(&mysql);
}

void park_db_insert(Video vid) {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;
	char park_buf[500];
	char exist_query[100];
	int x;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	sprintf(park_buf, "insert into park (park_num, park_name, park_size, park_length, park_mktime, park_resolution, park_url) values(%d,\'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\') ON DUPLICATE KEY UPDATE park_name=\'%s\', park_size=%d, park_mktime=\'%s\'", vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl(), vid.getName(), vid.getSize(), vid.getMakeTime());

	cout << park_buf << endl;
	if (mysql_query(&mysql, park_buf) != 0) {
		cout << "Insert Error" << endl;
	}
	cout << "Insert Success!!" << endl;
	mysql_close(&mysql);
}

void manl_db_insert(Video vid) {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;
	char manl_buf[500];
	char exist_query[100];
	int x;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	sprintf(manl_buf, "insert into manl (manl_num, manl_name, manl_size, manl_length, manl_mktime, manl_resolution, manl_url) values(%d,\'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\') ON DUPLICATE KEY UPDATE manl_name=\'%s\', manl_size=%d, manl_mktime=\'%s\'", vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl(), vid.getName(), vid.getSize(), vid.getMakeTime());

	cout << manl_buf << endl;
	if (mysql_query(&mysql, manl_buf) != 0) {
		cout << "Insert Error" << endl;
	}
	cout << "Insert Success!!" << endl;
	mysql_close(&mysql);
}

void impt_db_insert(Video vid) {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;
	char impt_buf[500];
	char exist_query[100];
	int x;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	sprintf(impt_buf, "insert into impt (impt_num, impt_name, impt_size, impt_length, impt_mktime, impt_resolution, impt_url) values(%d,\'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\') ON DUPLICATE KEY UPDATE impt_name=\'%s\', impt_size=%d, impt_mktime=\'%s\'", vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl(), vid.getName(), vid.getSize(), vid.getMakeTime());

	cout << impt_buf << endl;
	if (mysql_query(&mysql, impt_buf) != 0) {
		cout << "Insert Error" << endl;
	}
	cout << "Insert Success!!" << endl;
	mysql_close(&mysql);
}

void db_select() {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;

	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	mysql_query(&mysql, "SELECT * FROM norm");
	res = mysql_store_result(&mysql);
	fields = mysql_num_fields(res);

	while (row = mysql_fetch_row(res))
	{
		for (i = 0; i < fields; i++) {
			cout << row[i] << ' ';
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
}
void db_update() {
	MYSQL mysql;
	MYSQL_RES* res;
	int fields;
	int i;
	char norm_up[200];
	char impt_up[200];
	char park_up[200];
	char manl_up[200];
	char set[20];
	mysql_init(&mysql);

	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 0, NULL, 0);
	sprintf(set, "set @cnt = 0");
	sprintf(norm_up, "update norm set norm_num = @cnt:=@cnt+1");
	sprintf(impt_up, "update impt set impt_num = @cnt:=@cnt+1");
	sprintf(park_up, "update park set park_num = @cnt:=@cnt+1");
	sprintf(manl_up, "update manl set manl_num = @cnt:=@cnt+1");
	mysql_query(&mysql, set);
	mysql_query(&mysql, norm_up);
	cout << "Update Success!!" << endl;
	mysql_query(&mysql, set);
	mysql_query(&mysql, impt_up);
	cout << "Update Success!!" << endl;
	mysql_query(&mysql, set);
	mysql_query(&mysql, park_up);
	cout << "Update Success!!" << endl;
	mysql_query(&mysql, set);
	mysql_query(&mysql, manl_up);
	cout << "Update Success!!" << endl;
	mysql_close(&mysql);
}
void FileList(char* ip_addr, char* PATH)
{
	DIR* video_dir = NULL;
	struct dirent* entry;
	struct stat buf;

	int num = 1;
	char newpath[100];
	char vidpath[100];

	char webpath[100];
	char web_npath[70];
	char web_ipath[70];
	char web_ppath[70];
	char web_mpath[70];

	strcpy(vidpath, PATH);
	char* dirname[10] = { NULL, };   
	int i = 0;                     

	char* ptr = strtok(PATH, "\/");   

	while (ptr != NULL)           
	{
		dirname[i] = ptr;             
		i++;                       
		ptr = strtok(NULL, "\/");   
	}

	printf("%s\n", dirname[5]);
	if (strcmp(dirname[5], "Normal") == 0) {
		sprintf(web_npath, "http://%s/Upload/UB_video/Normal", ip_addr);
	}
	else if (strcmp(dirname[5], "Impact") == 0) {
		sprintf(web_npath, "http://%s/Upload/UB_video/Impact", ip_addr);
	}
	else if (strcmp(dirname[5], "Parking") == 0) {
		sprintf(web_npath, "http://%s/Upload/UB_video/Parking", ip_addr);
	}
	else if (strcmp(dirname[5], "Manual") == 0) {
		sprintf(web_npath, "http://%s/Upload/UB_video/Manual", ip_addr);
	}

	cout << "URL : " << web_npath << endl;
    cout << vidpath << endl;
	video_dir = opendir(vidpath);

	if (video_dir != NULL) {
		while ((entry = readdir(video_dir)) != NULL) {
			Video vid = Video();

			char* ext;
			ext = strrchr(entry->d_name, '.');
			if (strcmp(ext, ".mp4") == 0) {
				sprintf(webpath, "%s/%s", web_npath, entry->d_name);
				sprintf(newpath, "%s/%s", vidpath, entry->d_name);	// Combine Path
				stat(newpath, &buf);
	
				VideoCapture cap(newpath);				// Video File Analysis
				int width = cvRound(cap.get(CAP_PROP_FRAME_WIDTH));	// Video Info : Frame Width 
				int height = cvRound(cap.get(CAP_PROP_FRAME_HEIGHT));	// Video Info : Frame Height 
				double fps = cap.get(CAP_PROP_FPS);			// Video Info : FPS
				int fcount = cvRound(cap.get(CAP_PROP_FRAME_COUNT));	// Video Info : Frame Count
				int length = fcount / fps;				// Video Info : Video Length

				// Convert & Combine String
				char vidMakeTime[100];
				char vidLength[10];
				char vidResolution[20];
				char file[100];

				time_t t;
				t = buf.st_mtime;

				strcpy(file, entry->d_name);				// Copy Filename 
				sprintf(vidLength, "%d", length);			// Convert Integer to String
				sprintf(vidResolution, "%dX%d", width, height);		// Convert & Combine
				strcpy(vidMakeTime, timeToString(localtime(&t)));	// Convert time to String

				vid.setVideo(num, file, buf.st_size, vidLength, vidMakeTime, vidResolution, webpath);	// Set
				vid.printInfo();	// Output Video Info 
				if (strcmp(dirname[5], "Normal") == 0) {
					norm_db_insert(vid);
				}
				else if (strcmp(dirname[5], "Impact") == 0) {
					impt_db_insert(vid);
				}
				else if (strcmp(dirname[5], "Parking") == 0) {
					park_db_insert(vid);
				}
				else if (strcmp(dirname[5], "Manual") == 0) {
					manl_db_insert(vid);
				}
				db_update();
				// db_select();
				num++;
			}
		}
		closedir(video_dir);
	} else {
		perror("");
	}
}
char* timeToString(struct tm* t) {
	static char s[20];
	sprintf(s, "%04d-%02d-%02d %02d:%02d:%02d", t->tm_year + 1900, t->tm_mon + 1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	return s;
}

int main(int argc, char** argv)
{
	char NORM_PATH[50] = "/var/www/html/Upload/UB_video/Normal";
	char IMPT_PATH[50] = "/var/www/html/Upload/UB_video/Impact";
	char PARK_PATH[50] = "/var/www/html/Upload/UB_video/Parking";
	char MANL_PATH[50] = "/var/www/html/Upload/UB_video/Manual";

	char ip_addr[17];
	strcpy(ip_addr, argv[1]);
	if (argv[1] == NULL)
		cout << "error" << endl;

	FileList(ip_addr, NORM_PATH);
	FileList(ip_addr, IMPT_PATH);
	FileList(ip_addr, PARK_PATH);
	FileList(ip_addr, MANL_PATH);
}
