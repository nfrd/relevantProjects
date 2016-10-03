/*
 * fsh.c - the Feeble SHell.
 */
#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <fcntl.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include "fsh.h"
#include "parse.h"
#include "error.h"
#include "builtin.h"

int showprompt = 1;
int laststatus = 0;
extern char ** environ;
/* search takes in the executable name, first directory it's in, the current
 parsed line, and an int. The int is a check to see if it needs to run current 
 command or the p->pl->next->argv (piped command) */
int search(char *cmd,char *dir, struct parsed_line *p, int i);
extern void docommand(struct parsed_line *p);

int pid, status;
struct stat statbuf;

int main()
{
    char buf[1000];
    struct parsed_line *p;
    extern void execute(struct parsed_line *p);
    while (1) {
        if (showprompt)
            printf("$ ");
        if (fgets(buf, sizeof buf, stdin) == NULL)
            break;
        if ((p = parse(buf))) {
            execute(p);
            freeparse(p);
        }
    }
    return(laststatus);
}

void execute(struct parsed_line *p)
{
    for (; p; p = p->next) {
        fflush(stdout);

        /* check for connective and last status*/
        if ((laststatus == 0 && p->conntype == CONN_AND) || (laststatus != 0 && p->conntype == CONN_OR) || p->conntype==CONN_SEQ) {
            
            if (p->pl && !strcmp(p->pl->argv[0], "exit")) {
                laststatus=builtin_exit(p->pl->argv);
            }
            
            else if(p->pl && !strcmp(p->pl->argv[0], "cd")){
                if (p->pl->argv) {
                    laststatus=builtin_cd(p->pl->argv);
                }
                else{
                    laststatus=builtin_cd(p->pl->argv);
                }
            }
            else{
                int x = fork();
                if (x == -1){
                    perror("fork");
                }
                
                else if (x ==0){
                    if (p->inputfile){
                        close(0);
                        if ((open(p->inputfile, O_RDONLY, 0666)) < 0) {
                            laststatus=1;
                            perror(p->inputfile);
                        }
                    }
                    if (p->outputfile){
                        close(1);
                        if (open(p->outputfile, O_WRONLY|O_CREAT|O_TRUNC, 0666) < 0) {
                            laststatus=1;
                            perror(p->outputfile);
                        }
                    }
                    
                    if (p->pl){
                        if (p->pl->next){
                            /* pipe command */
                            docommand(p);
                        }
                        /* checks if the exectuable exists; if it does it runs the
                         executable, if not laststatus is set to 1 and prints error
                         message */
                        if (search(p->pl->argv[0], "/bin/", p,0) && search(p->pl->argv[0], "/usr/bin/",p,0)
                            && search(p->pl->argv[0], "",p,0)) {
                            laststatus=1;
                            fprintf(stderr, "%s: Command not found\n", p->pl->argv[0]);
                        }
                    }
                    /* exit laststatus to appropriately run the pending commands */
                    exit(laststatus);
                }
                else{
                    int status, pid;
                    pid = wait(&status);
                    if (WIFEXITED(status)) {
                        laststatus=(WEXITSTATUS(status));
                        
                    }
                }
            }
        }
    }
}

void docommand(struct parsed_line *p)  {
    int pipefd[2];
    
    if (pipe(pipefd)) {
        perror("pipe");
        exit(127);
    }
    
    switch (fork()) {
        case -1:
            perror("fork");
            exit(127);
        case 0:
            
            close(pipefd[0]);
            dup2(pipefd[1], 1);
            close(pipefd[1]);
            
            /* check and run exec (if it exists), change last status to appropriate
             int */
            if (search(p->pl->argv[0], "/bin/", p,0) && search(p->pl->argv[0], "/usr/bin/",p,0)
                && search(p->pl->argv[0], "",p,0)) {
                laststatus=1;
                
                fprintf(stderr, "%s: Command not found\n", p->pl->argv[0]);
                
            }
            exit(126);
        default:
            close(pipefd[1]);
            dup2(pipefd[0], 0);
            close(pipefd[0]);
            /* pass in int 1 at the fourth parameter because piped command 
             should be executed */
            if (search(p->pl->next->argv[0], "/bin/", p,1) && search(p->pl->next->argv[0], "/usr/bin/",p,1) && search(p->pl->next->argv[0], "",p,1)) {
                laststatus=1;
                
                fprintf(stderr, "%s: Command not found\n", p->pl->next->argv[0]);
                
            }
            exit(125);
    }
}

int search(char *cmd,char *dir, struct parsed_line *p, int i)
{
    char buf[1000];
    if (strlen(cmd) + strlen(dir) + 1 > sizeof buf) {
        fprintf(stderr, "buffer size exceeded\n");
        exit(1);
    }
    strcpy(buf, dir);
    strcat(buf, cmd);
    if (stat(buf, &statbuf) || (statbuf.st_mode & 0100) == 0){
        laststatus=1;
        return(1);}
    /* check if the argv[0] contains "/" */
    if (!strchr(p->pl->argv[0], '/')) {
        /* check if the next argv needs to be run or current*/
        if (i == 1) {
            laststatus=execve(buf, p->pl->next->argv, environ);
            perror(buf);
        }else if (i==0){
            laststatus=execve(buf, p->pl->argv, environ);
            perror(buf);
        }
    }
    else if (strchr(p->pl->argv[0], '/')){
        if (i==1) {
            laststatus=execve(cmd, p->pl->next->argv, environ);
            perror(cmd);
        }
        else if(i==0){
            laststatus=execve(cmd, p->pl->argv, environ);
            perror(cmd);
            
        }
    }
    return(0);
}
