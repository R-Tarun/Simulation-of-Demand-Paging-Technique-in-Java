import java.io.*;
import java.util.*;  
import java.lang.*;

class page{        
	int pgno,fno,rb,db;    
	public page(int pgno, int fno, int rb, int db) {        
    	this.pgno = pgno;
		this.fno=fno;
		this.rb=rb;
		this.db=db;      
	}
}      

class pagetable{        
	page[] p;  
	public pagetable(page[] p) {        
	    this.p = p;    
	}
}      

class memory{
	int pid,pno,t,cont;
	public memory(int pid, int pno, int t, int cont){
	this.pid=pid;
	this.pno=pno;
	this.t=t;
	this.cont=cont;
	}
}


public class Demand_Paging {
	public File createFile(final String filename, final long sizeInBytes) throws IOException {
		File file = new File(filename);
		file.createNewFile();
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		raf.setLength(sizeInBytes);
		raf.close();
		return file;
	}

	public static void main(String[] args) throws IOException {

		//initialization
		File f1 = new Demand_Paging().createFile("B_S.txt",1000);
		File f2 = new Demand_Paging().createFile("S_A.txt",100);
		RandomAccessFile raf = new RandomAccessFile(f1, "rw");
		Hashtable<String, Integer> dir = new Hashtable<>();
		memory m[] = new memory[50];
		for(int l=0;l<50;l++){
			m[l]=new memory(-1,-1,-1,-1);
		}
		String pid,pgn,str;
		int ofs,tpf=0,time=1,count=0;
		System.out.print("Enter no of Processes: ");
		Scanner s = new Scanner(System.in);
		int pcs=s.nextInt();
		int k=1,rl,pn;
		int pc[] = new int[pcs];
		pagetable pt[]=new pagetable[pcs];
		for (int i=0; i<pcs; i++){
			pid=String.valueOf(i);
			System.out.print("\nEnter no of Pages for Process"+i+": ");
			int pgs=s.nextInt();
			pc[i]=pgs;
			page pr[] = new page[pgs];
			for (int j=0; j<pgs; j++){
				pr[j] = new page(j,-1,0,0);
				pgn=String.valueOf(j);
				str=pid+pgn;
				ofs=(int)raf.getFilePointer();
				dir.put(str,ofs);
				raf.writeInt(k);
				k++;
			}
			pt[i] = new pagetable(pr);
		}
		//initialization
		while(true){
			System.out.print("\nEnter -1 to exit or Process_ID:");
			int pf=0;
			pn=s.nextInt();
			if(pn==-1){
				break;
			}
			System.out.print("\nEnter length of Reference String:");
			rl=s.nextInt();

			int ref[] = new int[rl];
			for(int j=0;j<rl;j++){
				ref[j]=s.nextInt();
			}
			for(int j=0;j<rl;j++){
				String s1;
				if (pt[pn].p[ref[j]].fno==-1){
					pf+=1;
					tpf+=1;
					if(count<50){
						for(int l=0;l<50;l++){
							if(m[l].pno==-1){
								m[l].pid=pn;
								m[l].pno=ref[j];
								m[l].t=time;
								s1=String.valueOf(pn)+String.valueOf(ref[j]);
								m[l].cont=dir.get(s1);
								count++;
								pt[pn].p[ref[j]].fno=l;
								pt[pn].p[ref[j]].pgno=ref[j];
								pt[pn].p[ref[j]].rb=1;
								break;
							}
						}
					}else{
						int min=999,mi=0;
						for(int l=0;l<50;l++){
							if (m[l].t<min){
								min=m[l].t;
								mi=l;
							}
						}
						pt[m[mi].pid].p[m[mi].pno].fno=-1;
						System.out.println("\nPage"+m[mi].pno+" of Process"+m[mi].pid+" is removed...");
						m[mi].pid=pn;
						m[mi].pno=ref[j];
						m[mi].t=time;
						s1=String.valueOf(pn)+String.valueOf(ref[j]);
						m[mi].cont=dir.get(s1);
						pt[pn].p[ref[j]].fno=mi;
						pt[pn].p[ref[j]].pgno=ref[j];
						pt[pn].p[ref[j]].rb=1;
					}
				}
			time+=1;
			}
			System.out.println("Page faults for process "+pn+": "+pf);
			int swit;
			System.out.print("Enter 1 to print Memory and Page Table... ");
			swit = s.nextInt();
			if (swit==1){
				System.out.println("\n--------------------------------------------------------");
				System.out.println("\t\t\tMEMORY");
				System.out.println("--------------------------------------------------------\n");
				System.out.println("\t\tPID\tPGNO\tDATA\n");
				for(int l=0;l<50;l++){
					if(m[l].pid!=-1){
						System.out.println("\t\t"+m[l].pid+"\t"+m[l].pno+"\t"+m[l].cont);
					}else{
						break;
					}
				}

				System.out.println("\n--------------------------------------------------------");
				System.out.println("\t\t\tPAGE TABLE");
				System.out.println("--------------------------------------------------------\n");
				System.out.println("\t\tPGNO\tFNO\tREF\n");
				for(int l=0;l<pc[pn];l++){
					System.out.println("\t\t"+pt[pn].p[l].pgno+"\t"+pt[pn].p[l].fno+"\t"+pt[pn].p[l].rb);
				}
			}
		}
		raf.close();
		System.out.println("Total Page faults occured: "+tpf);	 
	}
}
