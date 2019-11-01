#include<windows.h>
#include <GL/glut.h>
#include <stdlib.h>

void init(void)
{
	glClearColor (0.0, 0.0, 0.0, 0.0);
	glShadeModel (GL_SMOOTH);
}

void draw_triangle(void)
{
	glShadeModel(GL_SMOOTH);
	glColor3f(0.0,0.0,1.0);
	glBegin (GL_TRIANGLES);//画出三角形，为混合色填充方式
	glVertex2f(50.0, 25.0);
	glColor3f(0.4,0.5,0.60);
	glVertex2f(150.0, 25.0);
	glColor3f(0.9,0.7,0.8);
	glVertex2f(100.0, 100.0);
	glEnd();
}
void display(void)
{
	glClear (GL_COLOR_BUFFER_BIT);
	glColor3f (1.0, 1.0, 1.0);

	glLoadIdentity ();
	glColor3f (1.0, 1.0, 1.0);
	glTranslatef(-100.0,-50.0,1.0);
	draw_triangle ();

	glLoadIdentity ();
	glTranslatef (0.0, 100.0, 1.0);
	draw_triangle ();
	glLoadIdentity ();
	glRotatef (90.0, 0.0, 0.0, 1.0);
	draw_triangle ();
	glLoadIdentity ();
	glScalef (0.5, 0.5, 1.0);

	draw_triangle ();

	glFlush ();
}
/*
void display(void)
{
	glClear (GL_COLOR_BUFFER_BIT);
	glColor3f (1.0, 1.0, 1.0);

	glLoadIdentity ();
	glColor3f (1.0, 1.0, 1.0);
	glTranslatef(-100.0,-50.0,1.0);
	draw_triangle ();

	glLoadIdentity ();
	glTranslatef (0.0, 100.0, 1.0);
	glRotatef (90.0, 0.0, 0.0, 1.0);
	glScalef (0.5, 0.5, 1.0);
	draw_triangle ();//经过三种变换后画出图形

	glFlush ();
}
*/
void reshape (int w, int h)
{
	glViewport (0, 0, (GLsizei) w, (GLsizei) h);
	glMatrixMode (GL_PROJECTION);
	glLoadIdentity ();
	if (w <= h)
		gluOrtho2D (-200.0, 250.0, -100.0*(GLfloat)h/(GLfloat)w,
		200.0*(GLfloat)h/(GLfloat)w);//调整裁剪窗口
	else
		gluOrtho2D (-200.0*(GLfloat)w/(GLfloat)h,
		250.0*(GLfloat)w/(GLfloat)h, -50.0, 200.0);
	glMatrixMode(GL_MODELVIEW);
}
int main(int argc, char** argv)
{
	glutInit(&argc, argv);
	glutInitDisplayMode (GLUT_SINGLE | GLUT_RGB);
	glutInitWindowSize (600, 600);
	glutInitWindowPosition (100, 100);
	glutCreateWindow (argv[0]);
	init ();
	glutDisplayFunc(display);
	glutReshapeFunc(reshape);
	glutMainLoop();
	return 0;
}