CC = g++
HADOOP_INSTALL = ../lib/native/CDH3u5
PLATFORM = Linux-i386-32
CPPFLAGS = -m32 -I$(HADOOP_INSTALL)/$(PLATFORM)/include

wordcount: wordcount.cc
	$(CC) $(CPPFLAGS) $< -Wall -L$(HADOOP_INSTALL)/$(PLATFORM)/lib -lhadooppipes \
	-lhadooputils -lpthread -g -O2 -o $@
