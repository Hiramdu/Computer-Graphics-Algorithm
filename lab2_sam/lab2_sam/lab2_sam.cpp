// lab2_sam.cpp : 定义控制台应用程序的入口点。
//


#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <math.h>
#include <assert.h>
#include <time.h>

#include<string>
#include<vector>
#include<iostream>
using namespace std;

// Windows include files 

#ifdef _WIN32
#include <windows.h>
#endif



// OpenGL include files 

#include <GLUT/GLUT.h>


typedef struct Vertex
{
	float x,y,z;
}Vertex;

typedef struct Facep
{
	int num;
	int order[3];
}Facep;
static char *filename="bird.off";
Vertex ver[10000];
Facep fap[20000];
int i,j,k,n_node,n_face,n_edge;
float scale=1,spin=0;
static int window_height = 800;
static int window_width = 800;

int readoff(const char* filename)
{
	FILE *fp;
	
	if(!(fp=fopen(filename,"r")))
	{
		fprintf(stderr,"Open fail");
		return 0;
	}
    char buffer[1024];
	if(fgets(buffer,1023,fp))
	{
		if(!strstr(buffer,"OFF"))
			{
				printf("It's not a OFF FILE");
				return 0;
		}
		
		if(fgets(buffer,1023,fp))
		{
			sscanf(buffer,"%d %d %d",&n_node,&n_face,&n_edge);
			
			for(i=0;i<n_node;i++)
			{
				fgets(buffer,1023,fp);
				sscanf(buffer,"%f%f%f",&ver[i].x,&ver[i].y,&ver[i].z);
			}
			for(i=0;i<n_face;i++)
			{
				fgets(buffer,1023,fp);
				int temp;
				sscanf(buffer,"%d%d%d%d",&fap[i].num,&fap[i].order[0],&fap[i].order[1],&fap[i].order[2]);
			}
		}

    }
}
void redraw(void)
{
	int a=clock();
	glClearColor(0.0, 0.0, 0.0, 0.0);
    
	glMatrixMode(GL_MODELVIEW);
	glLoadIdentity();
	glScalef(scale,scale,scale);
	glRotatef(spin,0.0,0.0,1.0);
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glColor3f(1.0,0.0,0.0);
	glBegin(GL_TRIANGLES);
	for(i=0;i<n_face;i++)
	{
		int count=fap[i].order[0];
		glVertex3f(ver[count].x,ver[count].y,ver[count].z);
		count=fap[i].order[1];
		glVertex3f(ver[count].x,ver[count].y,ver[count].z);
		count=fap[i].order[2];
		glVertex3f(ver[count].x,ver[count].y,ver[count].z);
	}
	glEnd();
	glFlush();
	glutSwapBuffers();
	int b=clock();
	a=b-a;
	printf("绘制时间：%d\n",a);
}
void keyboard(unsigned char key, int x, int y)
{
	switch(key)
	{
	   case 'p':glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);glutPostRedisplay();break;
       case 'l':glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);glutPostRedisplay();break;
       case 'f':glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);glutPostRedisplay();break;
	   case 'q':exit(1);
	}
}
void mouse(int button,int state,int x,int y)
{
	if(button==GLUT_LEFT_BUTTON && state==GLUT_DOWN)
	{
		scale*=1.1;
		glutPostRedisplay();
	}
	if(button==GLUT_RIGHT_BUTTON && state==GLUT_DOWN)
	{
		
		for(i=0;i<10;i++)
		{
			spin=spin+1;
		    glutPostRedisplay();
			glFlush();
		}
	}
}
void init(int *argc, char **argv)
{
  // Open window 
  glutInit(argc, argv);
  glutInitWindowPosition(100, 100);
  glutInitWindowSize(window_width, window_height);
 
  glutCreateWindow("OFF MADE BY SAM JJX");

  // Initialize GLUT callback functions 
  glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
  //glutReshapeFunc(GLUTResize);
  glutDisplayFunc(redraw);
  glutKeyboardFunc(keyboard);
  glutMouseFunc(mouse);
  glPolygonMode(GL_FRONT_AND_BACK, GL_POINT);
  //glutMotionFunc(motion);
  glutIdleFunc(0);


}

int main(int argc, char** argv)
{
	init(&argc,argv);
	
	readoff(filename);
	glutMainLoop();
	return 0;
}

