#_*_coding:utf8_*_

li1=[2,'b','d',2,'wer',1,2,4,5,6,2,7,8,9,2,4,5,6,2,2,2,2,2]

def find_ele2(li,ele):
	'''find elements index in list and use index func'''
	tmp=li[:]
	result=[]
	ele_count = tmp.count(ele)
	index=-1
	for i in range(ele_count):		
		if index == -1:
			index = tmp.index(ele)
		else:
			index = tmp.index(ele,index+1)
		result.append(index)
	return result

def find_ele1(li,ele):
	'''find elements index in list and don't use index func'''
	tmp=li[:]
	result=[]
	ele_count = tmp.count(ele)	
	flag=0
	for i in range(ele_count):
		index = tmp.index(ele)
		tmp=tmp[index+1:]
		if i != 0:
			flag+=1
		index += flag				
		flag = index
		result.append(index)
	return result
	
def find_ele3(li,ele):
    return [index for index,item in enumerate(li) if item==ele]
	
print find_ele1(li1,2)
print '\n'
print find_ele2(li1,2)
print '\n'
print find_ele3(li1,2)