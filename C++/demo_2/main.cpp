#include <unistd.h>
#include <stdio.h>
#include <iostream>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <string.h>

using namespace std;

int main(void){
    int filedes[2];
    char buf[80];
    pid_t pid ;

    pipe(filedes);
    pid = fork();
    if(pid>0){cout<<"this is the father process ,here write a string to the pipe"<<endl;
        char s[] = "hello world, this is writen by pipe";
        //write(filedes[1],s,sizeof(s));
        send(filedes[1],s,sizeof(s),0);

        close(filedes[0]);
        close(filedes[1]);
    }
    else if(pid == 0){
        cout<<"this is in the child process ,here read a string from the pipe"<<endl;
        //read(filedes[0],buf,sizeof(buf));
        recv(filedes[1],buf,sizeof(buf),0);
        cout<<buf<<endl;
        close(filedes[0]);
        close(filedes[1]);
    }
    waitpid(pid,NULL,0);
    return 0;
}