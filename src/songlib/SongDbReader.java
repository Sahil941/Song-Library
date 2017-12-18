//Malik Ameer(mma174) & Sahil Kumbhani(srk112)

package songlib;

import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;

public class SongDbReader {
	String fileName = "db.txt";
	FileWriter fw;
	FileReader fr;
	BufferedWriter bw;
	Scanner input;
	
	public SongDbReader(){
		try{
			this.fw = new FileWriter(fileName, true);
			this.fr = new FileReader(fileName);
			this.bw = new BufferedWriter(fw);
			this.input = new Scanner(fr);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	//Checks to see if a song exists in the text file
	//and returns it, otherwise returns null
	public Song nextSong() throws IOException {
		if(input.hasNextLine()){
			Song song = new Song();
			String str = this.input.nextLine();
			String title = str.substring(0, str.indexOf("|"));
			String artist = str.substring(str.indexOf("|") + 1);
			song.title = title;
			song.artist = artist;
			return song;
		}
		else{
			return null;
		}
	}
	
	//Truncates the current database text file and rewrites
	//it with the current data from the song library
	public void databaseWriter(Object[] o) throws IOException{
		FileWriter fwTemp = new FileWriter(fileName, false);
		BufferedWriter bwTemp = new BufferedWriter(fwTemp);
		bwTemp.write("");
		bwTemp.flush();
		bwTemp.close();
		fwTemp.close();
		
		for (int i = 0; i < o.length; i++){
			Song song = (Song)o[i];
			this.bw.write(song.title + "|" + song.artist);
			this.bw.newLine();
		}
	}
	
	//Closes all the open FileWriters, FileReaders, BufferedWriter and Scanner
	public void databaseCloser() throws IOException{
		this.input.close();
		this.fr.close();
		this.bw.close();
		this.fw.close();
	}
}