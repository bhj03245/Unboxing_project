import os
import glob

norm_path = '/var/www/html/Upload/UB_video/Normal/'
impt_path = '/var/www/html/Upload/UB_video/Impact/'
park_path = '/var/www/html/Upload/UB_video/Parking/'
manl_path = '/var/www/html/Upload/UB_video/Manual/'

def file_list(in_path):
    vidlist = glob.glob(in_path + '/*.h264')
    return vidlist


def remove_cache(file):
    if len(file) == 0:
        return
    recording_file = max(file, key=os.path.getctime)
    for i in range(0, len(file)):
        file_size = os.path.getsize(file[i])
        if file_size == 0 and file[i] == recording_file:
            continue
        elif file_size == 0:
            print("Delete")
            os.remove(file[i])
        else:
            print(file[i])

norm_file = file_list(norm_path)
impt_file = file_list(impt_path)
park_file = file_list(park_path)
manl_file = file_list(manl_path)

remove_cache(norm_file)
remove_cache(impt_file)
remove_cache(park_file)
remove_cache(manl_file)
