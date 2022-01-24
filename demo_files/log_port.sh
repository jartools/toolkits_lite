#!/bin/sh
log_port(){
	port=$1;
	dstr=`date +'%Y%m%d_%H%M%S'`;
	sudo netstat -anp | grep $port > $dstr.log;
	exit 0;
}
[ $# -ne 1 ] && exit 1
log_port $1;
# 放在 /home/fd_sh/ 下面，要有执行权限
# chmod +x /home/fd_sh/log_port.sh