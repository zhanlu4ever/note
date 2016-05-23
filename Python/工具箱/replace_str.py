#_*_coding:utf8_*_

import sys,os,shutil

# 参数校验

ERROR_INFO='''
usage: >>>./replace_str.py old_text new_text SRC_FILE
usage: >>>./replace_str.py old_text new_text SRC_FILE --bkup
usage: >>>./replace_str.py old_text new_text SRC_FILE --new
usage: >>>./replace_str.py old_text new_text SRC_FILE --new new_file_name
usage: >>>./replace_str.py old_text new_text SRC_FILE --bkup --new new_file_name
'''

ARGS_COUNT=len(sys.argv)

OLD_TEXT,NEW_TEXT,SRC_FILE = sys.argv[1],sys.argv[2],sys.argv[3]

def read_value_from_file():
	global OLD_TEXT
	global NEW_TEXT
	lines=[]
	with open(SRC_FILE,'r+b') as local_file:		
		for line in local_file.xreadlines():
			line = line.replace(OLD_TEXT,NEW_TEXT)
			lines.append(line)
	
	return lines

def value_replace():	
	global SRC_FILE
	global ARGS_COUNT
	lines =	read_value_from_file()
	
	if ARGS_COUNT == 4:
		with open(SRC_FILE,'r+b') as local_file:
			local_file.writelines(lines)
	elif ARGS_COUNT == 5:		
		if sys.argv[4] == '--bkup':
			src_local_file = open(SRC_FILE,'r+b')
			shutil.copy(SRC_FILE,'%s.bak' % SRC_FILE)
			src_local_file.writelines(lines)
		elif sys.argv[4] == '--new':
			# D:/py/code1/names.log
			SRC_FILE = SRC_FILE[:SRC_FILE.rfind('/')+1]+'new_'+SRC_FILE[SRC_FILE.rfind('/')+1:]
			new_file = open(SRC_FILE,'w+b')
			new_file.writelines(lines)
	elif ARGS_COUNT == 6:
		src_file_path = SRC_FILE[:SRC_FILE.rfind('/')+1]
		new_file = open(src_file_path+sys.argv[5],'w+b')
		new_file.writelines(lines)			
	elif ARGS_COUNT == 7:
		src_file_path = SRC_FILE[:SRC_FILE.rfind('/')+1]
		#src_local_file = open(SRC_FILE,'r+b')
		shutil.copy(SRC_FILE,'%s.bak' % SRC_FILE)
		new_file = open(src_file_path+sys.argv[6],'w+b')
		new_file.writelines(lines)

def file_exists_check():
	'''检查文件是否存在'''
	global ARGS_COUNT
	global ERROR_INFO
	if ARGS_COUNT > 3 and os.path.exists(sys.argv[3]) == False:	
		raise ValueError('file not exists : %s' % ERROR_INFO)
	
def args_empty_check():
	'''检查参数是否为空'''
	global ERROR_INFO
	for arg in sys.argv:				
		if '' == arg.strip():
			raise ValueError('error info args should not be empty : %s' % ERROR_INFO)
		
def args_check():
	'''arguments check main call'''	
	args_empty_check()	
	file_exists_check()
	args_count_check()
				
def args_count_check():
	'''参数个数校验'''
	global ARGS_COUNT
	global ERROR_INFO
	
	msg = ''	
	if ARGS_COUNT < 4:
		msg='arguments error '				
	elif ARGS_COUNT == 5:
		if '--bkup' != sys.argv[4] and '--new' != sys.argv[4]:
			msg='error info 2'
	elif ARGS_COUNT==6:
		if '--new' != sys.argv[4]:
			msg='error info 3'
	elif ARGS_COUNT==7:
		if '--bkup' != sys.argv[4]:
			msg='error info 4'			
		elif '--new' != sys.argv[5]:
			msg='error info 5'
	if msg != '':
		raise ValueError('%s : %s' % (msg,ERROR_INFO))

def main():
	'''主调函数'''	
	args_check()
	value_replace()
	
main()