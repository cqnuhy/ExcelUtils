# -*- coding: utf-8 -*-
import os

package = ''

def java(binPath,classname,libPath=None):
    srcPath = os.path.join(os.path.join(binPath+'/../'),'src')
    if not libPath:
        libPath = os.path.join(os.path.join(binPath+'/../'),'lib')
    classPath = classpath(libPath)
    findpackage(srcPath,classname)
    print package
    tclass = package+'.'+classname
    print tclass
    print "java -classpath "+binPath+';'+classPath+' '+tclass
    os.system("java -classpath "+binPath+';'+classPath+' '+tclass)

def javac(srcPath,binPath=None,libPath=None,srcbin=False):
    '编译项目下的java文件'
    srcPath = os.path.dirname(srcPath)
    if not srcPath:
        return False
    if not binPath:
        if srcbin:
            binPath = srcPath
        else:
            #print os.pardir(os.path.dirname(srcPath))
            binPath = os.path.join(os.path.join(srcPath+'/../'),'bin')
            if not os.path.exists(binPath):
                os.makedirs(binPath)
            print binPath
    if not libPath:
        libPath = os.path.join(os.path.join(srcPath+'/../'),'lib')

    classPath = classpath(libPath)
    srcDic = {}
    binDic = {}
    srcDic = fileDic(srcPath,srcDic,'.java')
    binDic = fileDic(binPath,srcDic,'.class')

    srcKeys = srcDic.keys()
    for src in srcKeys:
        print '开始编译文件 ',src
        os.system("javac -encoding utf-8 -classpath "+srcPath+';'+classPath+" -d "+binPath+' '+srcDic.get(src))
        print src,' 编译完成'

def classpath(libPath):
    '合成classpath'
    libdir = os.listdir(libPath)
    classPath = ''
    for lib in libdir:
        pathname = os.path.join(libPath,lib)
        if not os.path.isfile(pathname):
            classpath(pathname)
        else:
            if pathname.endswith('.class') or pathname.endswith('.jar'):
                classPath += pathname + ';'
    return classPath

def fileDic(filepath,filedic,filetype):
    '返回文件字典，文件名:文件路径'
    li = os.listdir(filepath)
    for filename in li:
         pathname = os.path.join(filepath,filename)
         if not os.path.isfile(pathname):
             fileDic(pathname,filedic,filetype)
         else:
             if filename.endswith(filetype):
                 filedic[filename.split('.')[0]] = pathname
    return filedic

def findpackage(filepath,classname):
    '寻找类'
    li = os.listdir(filepath)
    for filename in li:
         pathname = os.path.join(filepath,filename)
         if not os.path.isfile(pathname):
            findpackage(pathname,classname)
         else:
             if classname+'.java' == filename:
                 f = file(pathname)
                 while True:
                     line = f.readline()
                     if len(line) == 0:
                         break
                     strs = line.split(' ')
                     if strs[0] == 'package':
                         global package
                         package = strs[1].split(';')[0]
                         break
                 f.close()
    return package

#javac("D:/workspace/bigexcel/src/")
java("D:/workspace/bigexcel/bin/",'HxlsPrint')
