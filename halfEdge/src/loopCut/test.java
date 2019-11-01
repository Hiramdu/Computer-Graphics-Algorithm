package loopCut;

import java.io.*;
import java.util.ArrayList;

public class test {

	BufferedReader br = null;
	BufferedWriter bw = null;
	String s = null;
	int vert_num = -1;
	int face_num = -1;
	ArrayList<HE_vert> vertList = new ArrayList<HE_vert>();
	ArrayList<HE_edge> edgeList = new ArrayList<HE_edge>();
	ArrayList<HE_face> faceList = new ArrayList<HE_face>();
	
	ArrayList<HE_vert> oldVertNewPositionList;
	ArrayList<New_vert> newVertList;

	public test(String source,String destin) {
		try {
			// ����FileReader����
			FileReader fr = new FileReader("D:\\c++\\off\\" + source);
			br = new BufferedReader(fr);

			// ����FileWriter����
			FileWriter fw = new FileWriter("D:\\c++\\off\\" + destin, false);
			bw = new BufferedWriter(fw);

			s = br.readLine();
			if (!s.equals("OFF")) {
				System.out.println("�ⲻ��.off�ļ�");
			} else {
				// ���Ȼ�õ���������
				s = br.readLine();
				String[] numString = s.split(" ");
				vert_num = Integer.parseInt(numString[0]);
				face_num = Integer.parseInt(numString[1]);
				if (vert_num < 0 || face_num < 0)
					System.out.println("��������������");
				else {
					System.out.println("������" + vert_num + "��������" + face_num);
					// ��ʼ��ʼ����߽ṹ
					// �ȳ�ʼ�㲿��(��Ҫ������)
					for (int i = 0; i < vert_num; i++) {
						s = br.readLine();
						String[] xyz = s.split(" ");
						float x = Float.parseFloat(xyz[0]);
						float y = Float.parseFloat(xyz[1]);
						float z = Float.parseFloat(xyz[2]);
						// �������б���
						vertList.add(new HE_vert(x, y, x));
					//	System.out.println("x:" + x + ",y:" + y + ",z:" + z);
					}
					// �ٳ�ʼ����ͱߺ͵�ı߲���
					for (int i = 0; i < face_num; i++) {
						// һ�����������а����ĸ�����һ������
						HE_face face = new HE_face();
						HE_edge edge1 = new HE_edge();
						HE_edge edge2 = new HE_edge();
						HE_edge edge3 = new HE_edge();
						s = br.readLine();
						String[] vert0123 = s.split(" ");
						int point_num = Integer.parseInt(vert0123[0]);
						int vert_id1 = Integer.parseInt(vert0123[1]);
						int vert_id2 = Integer.parseInt(vert0123[2]);
						int vert_id3 = Integer.parseInt(vert0123[3]);
						// ��ʼ����
						// ��������б���
						faceList.add(face);
						face.edge = edge1;
						// ��ʼ����
						// edge1
						edge1.begin_vert_id = vert_id1;
						edge1.end_vert_id = vert_id2;
						findPairAndInit(edge1);
						edge1.face = face;
						edge1.next = edge2;
						edgeList.add(edge1);
						// edge2
						edge2.begin_vert_id = vert_id2;
						edge2.end_vert_id = vert_id3;
						findPairAndInit(edge2);
						edge2.face = face;
						edge2.next = edge3;
						edgeList.add(edge2);
						// edge3
						edge3.begin_vert_id = vert_id3;
						edge3.end_vert_id = vert_id1;
						findPairAndInit(edge3);
						edge3.face = face;
						edge3.next = edge1;
						edgeList.add(edge3);
						// ��ʼ����ı߲���
						if (vertList.get(vert_id1).edge == null) {
							vertList.get(vert_id1).edge = edge1;
						}
						if (vertList.get(vert_id2).edge == null) {
							vertList.get(vert_id2).edge = edge2;
						}
						if (vertList.get(vert_id3).edge == null) {
							vertList.get(vert_id3).edge = edge3;
						}

					//	System.out.println("vert_id1:" + vert_id1 + ",vert_id2:" + vert_id2 + ",vert_id3:" + vert_id3);
					}
					//testPrint();
					meshRefinement();
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				br.close();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void meshRefinement() {
		// �����Ѵ��ڵĵ����λ�ú��µ�
		// �ɵ���λ�ô���б�
		oldVertNewPositionList = new ArrayList<HE_vert>();
		// ����µ���б�
		newVertList = new ArrayList<New_vert>();
		for (int i = 0; i < vert_num; i++) {
			// ����ɵ���λ��
			HE_vert vert = vertList.get(i);
			HE_vert NewVert = new HE_vert();
			// ��þɵ�Ķ���
			int n = getVertN(vert);
		//	System.out.println("��" + i + "������" + n);
			// ����bita
			double bita = (1.0 / n) * (5.0 / 8.0 - Math.pow((3.0 / 8.0 + 1.0 / 4.0 * Math.cos(2 * Math.PI / n)), 2));
		//	System.out.println("bita:" + bita);
			// ����ɵ���λ��
			NewVert.x = (float) ((1 - n * bita) * vert.x);
			NewVert.y = (float) ((1 - n * bita) * vert.y);
			NewVert.z = (float) ((1 - n * bita) * vert.z);
			HE_edge edge = vert.edge;
			do {
				NewVert.x += (float) (bita * vertList.get(edge.end_vert_id).x);
				NewVert.y += (float) (bita * vertList.get(edge.end_vert_id).y);
				NewVert.z += (float) (bita * vertList.get(edge.end_vert_id).z);
				edge = edge.pair.next;
			} while (edge != vert.edge);
			oldVertNewPositionList.add(NewVert);

			// �����µ�λ��
			edge = vert.edge;
			do {
				boolean exist = false;
				for (int a = 0; a < newVertList.size(); a++) {
					if (newVertList.get(a).begin == edge.begin_vert_id && newVertList.get(a).end == edge.end_vert_id) {
						exist = true;
						break;
					}
					if (newVertList.get(a).begin == edge.end_vert_id && newVertList.get(a).end == edge.begin_vert_id) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					HE_vert p = vert;
					HE_vert p0 = vertList.get(edge.end_vert_id);
					HE_vert p_1 = vertList.get(edge.next.end_vert_id);
					HE_vert p1 = vertList.get(edge.pair.next.end_vert_id);
					New_vert newVert = new New_vert();
					newVert.begin = edge.begin_vert_id;
					newVert.end = edge.end_vert_id;
					newVert.x = (3 * p.x + 3 * p0.x + p_1.x + p1.x) / 8;
					newVert.y = (3 * p.y + 3 * p0.y + p_1.y + p1.y) / 8;
					newVert.z = (3 * p.z + 3 * p0.z + p_1.z + p1.z) / 8;
					newVert.times++;
					newVertList.add(newVert);
				}
				edge = edge.pair.next;
			} while (edge != vert.edge);
		}
		
		//�����ɵ���д���ļ�
		writeIntoNewFile();	
	}

	

	private void writeIntoNewFile() {
		//�����ɵ���д���ļ�
				try {
					bw.write("OFF");
					bw.newLine();
					int vert_count = newVertList.size() + oldVertNewPositionList.size();
					int face_count = face_num * 4;
					int edge_count = 0;
					bw.write(vert_count + " " + face_count + " " + edge_count);
					bw.newLine();
					for(int i = 0;i < oldVertNewPositionList.size();i++){
						bw.write(oldVertNewPositionList.get(i).x + " " + oldVertNewPositionList.get(i).y + " " + oldVertNewPositionList.get(i).z);
						bw.newLine();
					}
					for(int i = 0;i < newVertList.size();i++){
						bw.write(newVertList.get(i).x + " " + newVertList.get(i).y + " " + newVertList.get(i).z);
						bw.newLine();
					}
					for(int i = 0;i < face_num;i++){
						HE_face face = faceList.get(i);
						HE_edge edge1 = face.edge;
						int id1 = getNewVertId(edge1);
						HE_edge edge2 = edge1.next;
						int id2 = getNewVertId(edge2);
						HE_edge edge3 = edge2.next;
						int id3 = getNewVertId(edge3);
						bw.write("3" + " " + edge1.begin_vert_id + " " + id1 + " " + id3);
						bw.newLine();
						bw.write("3" + " " + id1 + " " + edge2.begin_vert_id + " " + id2);
						bw.newLine();
						bw.write("3" + " " + id2 + " " + edge2.end_vert_id + " " + id3);
						bw.newLine();
						bw.write("3" + " " + id1 + " " + id2 + " " + id3);
						bw.newLine();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	}

	private int getNewVertId(HE_edge edge) {
		int index = oldVertNewPositionList.size() -1;
		int begin = edge.begin_vert_id;
		int end = edge.end_vert_id;
		for(int i=0;i<newVertList.size();i++){
			if(newVertList.get(i).begin == begin && newVertList.get(i).end == end){
				index += i;
				index++;
				break;
			}
			if(newVertList.get(i).end == begin && newVertList.get(i).begin == end){
				index += i;
				index++;
				break;
			}
		}
		if(index == oldVertNewPositionList.size() -1){
			System.out.println("��ͼ�����ҵ�ID�д�");
		}
		return index;
	}

	private int getVertN(HE_vert vert) {
		int n = 0;
		HE_edge edge = vert.edge;
		do {
			n++;
			edge = edge.pair.next;

		} while (edge != vert.edge);
		return n;
	}

	private void testPrint() {
		for (int i = 0; i < faceList.size(); i++) {
			System.out.print("��" + i + ": ");
			HE_face face = faceList.get(i);
			HE_edge edge = face.edge;
			do {
				if (edge == null) {
					System.out.println("�߲���Ϊnull�Ĵ���");
					return;
				}
				System.out.print("��:" + edge.begin_vert_id + "-" + edge.end_vert_id + " ");
				edge = edge.next;

			} while (edge != face.edge);
			System.out.println();
		}
		for (int i = 0; i < vertList.size(); i++) {
			System.out.println(
					"��" + i + ": x = " + vertList.get(i).x + " y = " + vertList.get(i).y + " z = " + vertList.get(i).z
							+ " �õ�ıߣ�" + vertList.get(i).edge.begin_vert_id + "--" + vertList.get(i).edge.end_vert_id);
		}
	}

	private void findPairAndInit(HE_edge edge) {
		// Ѱ���෴�ߣ��ҵ����ำֵ
		for (int i = 0; i < edgeList.size(); i++) {
			HE_edge temp = edgeList.get(i);
			// �෴������
			if (temp.begin_vert_id == edge.end_vert_id && temp.end_vert_id == edge.begin_vert_id) {
				if (temp.pair == null) {
					temp.pair = edge;
					edge.pair = temp;
				} else {
					System.out.println("ƥ���෴��ʱ�쳣");
				}
			}
		}

	}

	public static void main(String[] args) {
		for(int i = 0;i < 6;i++){
			int source = i;
			int destin = i+1;
			test test = new test("temp" + source + ".off","temp" + destin + ".off");
			System.out.println(i + "ȫ������");
		}
		System.out.println("ȫ������");
	}

}

class HE_vert {
	public float x = -1.0f;
	public float y = -1.0f;
	public float z = -1.0f;
	public HE_edge edge = null;// �Ըõ�Ϊ�ն˵�����һ�����
	// ���캯��

	public HE_vert() {
	}

	public HE_vert(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
}

class HE_edge {
	int begin_vert_id = -1;// ��߿�ʼ�ĵ��ID
	int end_vert_id = -1;// ����ն˵ĵ��ID
	HE_edge pair = null;// �෴����һ�����
	HE_face face = null;// ������ڵ���
	HE_edge next = null;// ͬһ���ڣ�˳(����)�뷽�����һ���
}

class HE_face {
	public HE_edge edge = null;// ���ڵ�ĳһ�����
}

class New_vert {
	int begin = -1;
	int end = -1;
	float x = -1.0f;
	float y = -1.0f;
	float z = -1.0f;
	int times = 0;
}