//Malik Ameer(mma174) & Sahil Kumbhani(srk112)

package songlib;

public class Song implements Comparable<Song>{
	
	String title;
	String artist;
	
	public Song(){
		
	}
	
	public Song(String title, String artist){
		this.title = title;
		this.artist = artist;
	}
	
	public String toString() {
		return (this.title + " - " + this.artist);
	}
	
	//Compares Title only -- used to sort Songs in alphabetical order
	@Override
	public int compareTo(Song other) {
		if(other == null || other.title.isEmpty() || this.title == null || other.artist.isEmpty() || this.artist == null) {
			return -1;
		}
		if(this.title.equals(other.title)){
			return this.artist.compareTo(other.artist);
		}
		return this.title.compareTo(other.title);
	}
	
	//Edit song name
	public void editTitle(String newTitle) {
		if(newTitle != null) {
			this.title = newTitle;
		}
	}
	
	//Edit song artist name
	public void editArtist(String newArtist) {
		if(newArtist != null) {
			this.artist = newArtist;
		}
	}
	//Edit song album name
	/*public void editAlbum(String newAlbum) {
		if(newAlbum != null) {
			this.title = newAlbum;
		}
	}*/
	
	//Equals function for Song
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Song)) {
			return false;
		}
		
		Song other = (Song)o;
		return title.equals(other.title) && 
				artist.equals(other.artist);
				//album.equals(other.album);
	}
}