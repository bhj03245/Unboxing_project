#include <sys/types.h>
#include <dirent.h>
#include <iostream>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <opencv4/opencv2/opencv.hpp>
#include <time.h>
#include <error.h>
#include "/usr/include/mysql/mysql.h"

#define DB_HOST "localhost"
#define DB_USER "root"
#define DB_PW "root"
#define DB_NAME "video"

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

void db_insert(Video vid);
void db_select();
void db_update();
char* timeToString(struct tm *t);

void db_insert(Video vid) {
	MYSQL mysql;
	MYSQL_RES* res;
	MYSQL_ROW row;
	int fields;
	int i;
	list <string> vlist;
	list<string>::iterator iter;
	MYSQL_FIELD* field;
	char buf[500];
	char exist_query[100];
	int x;
	mysql_init(&mysql);
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 3306, NULL, 0);
	//sprintf(buf, "insert into video.norm values""('%d','%s', '%d', '%s', '%s', '%s', '%s')",vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl());
	sprintf(buf, "insert into video.norm (norm_num, norm_name, norm_size, norm_length, norm_mktime, norm_resolution, norm_url) values('%d','%s', '%d', '%s', '%s', '%s', '%s') ON DUPLICATE KEY UPDATE norm_name='%s', norm_size='%d', norm_mktime='%s' order by norm_mktime", vid.getNum(), vid.getName(), vid.getSize(), vid.getLength(), vid.getMakeTime(), vid.getResolution(), vid.getUrl(), vid.getName(), vid.getSize(), vid.getMakeTime());
	mysql_query(&mysql, buf);
	cout << "Insert Success!!" << endl;
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
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 3306, NULL, 0);
	mysql_query(&mysql, "SELECT * FROM video.norm");
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
void db_update(){
	MYSQL mysql;
	MYSQL_RES* res;
	int fields;
	int i;
	char buf[200];
	char set[20];
	mysql_init(&mysql);
	
	mysql_real_connect(&mysql, DB_HOST, DB_USER, DB_PW, DB_NAME, 3306, NULL, 0);
	sprintf(set, "set @cnt = 0");
	sprintf(buf, "update video.norm set video.norm.norm_num = @cnt:=@cnt+1");
	mysql_query(&mysql, set);
	mysql_query(&mysql, buf);
	cout << "Update Success!!" << endl;
}
void FileList()
{
	DIR *dir = NULL;
	struct dirent* entry;
	struct stat buf;

	int num = 1;
	char newpath[100];	
	char NORM_PATH[50] = "/home/pi/Desktop/UB_video/Normal";
	char IMPT_PATH[50] = "/home/pi/Desktop/UB_video/Impact";
	char PARK_PATH[50] = "/home/pi/Desktop/UB_video/Parking";
	char MANL_PATH[50] = "/home/pi/Desktop/UB_video/Manual";

	char NormParentPath[100];	
	char NormCurrentPath[100];
	char ImptParentPath[100];	
	char ImptCurrentPath[100];
	char ParkParentPath[100];	
	char ParkCurrentPath[100];
	char ManlParentPath[100];	
	char ManlCurrentPath[100];

	sprintf(NormParentPath, "%s/..", NORM_PATH);
	sprintf(NormCurrentPath, "%s/.", NORM_PATH); 
	sprintf(ImptParentPath, "%s/..", IMPT_PATH);
	sprintf(ImptCurrentPath, "%s/.", IMPT_PATH); 
	sprintf(ParkParentPath, "%s/..", PARK_PATH);
	sprintf(ParkCurrentPath, "%s/.", PARK_PATH); 
	sprintf(ManlParentPath, "%s/..", MANL_PATH);
	sprintf(ManlCurrentPath, "%s/.", MANL_PATH); 

	norm_dir = opendir(NORM_PATH);
	impt_dir = opendir(IMPT_PATH);
	park_dir = opendir(PARK_PATH);
	manl_dir = opendir(MANL_PATH);
	
	if(norm_dir != NULL){
		while((entry = readdir(norm_dir)) != NULL){	
			Video vid = Video();	
			
			char *ext; 
			ext = strrchr(entry->d_name, '.'); 
			if(strcmp(ext, ".mp4") == 0) { 
				sprintf(newpath, "%s/%s", NORM_PATH, entry->d_name);	// Combine Path
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
				
				vid.setVideo(num, file, buf.st_size, vidLength, vidMakeTime, vidResolution, newpath);	// Set
				vid.printInfo();	// Output Video Info 
				db_insert(vid);
				db_update();
				// db_select();
				num++;
			}

		}		
	
		closedir(norm_dir);
		} else {
			perror("");
	}
}	

char* timeToString(struct tm *t){
	static char s[20];
	sprintf(s, "%04d-%02d-%02d %02d:%02d:%02d", t->tm_year + 1900, t->tm_mon + 1, t->tm_mday, t->tm_hour, t->tm_min, t->tm_sec);
	return s;
}

int main()
{
	FileList();
}
