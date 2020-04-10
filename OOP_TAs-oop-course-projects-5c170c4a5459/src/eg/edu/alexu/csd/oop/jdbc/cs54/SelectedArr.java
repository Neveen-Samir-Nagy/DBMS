package eg.edu.alexu.csd.oop.jdbc.cs54;

import java.util.Arrays;

public class SelectedArr implements Container {
	public Object[][] selected;
	SelectedArr(Object[][] selected){
		this.selected = selected;
		
	}
	
	@Override
	public Iterator getIterator() {
		return  new Arr();
	}
	
	private class Arr implements Iterator {
		
		int cursor = -1;
		@Override
		public boolean next() {
			if(selected.length==0) {
				return false;
			}
			cursor++;
			if(cursor<selected.length) {
				return true;
			}
			return false;
		}

		@Override
		public boolean previous() {
			if(selected.length==0) {
				return false;
			}
			cursor--;
			if(cursor>=0) {
				return true;
			}
			return false;
		}

		@Override
		public boolean last() {
			if(selected.length==0) {
				return false;
			}
			cursor = selected.length-1;
			return true;
		}

		@Override
		public boolean first() {
			if(selected.length==0) {
				return false;
			}else {
				cursor = 0;
			}
			return true;
		}

		@Override
		public boolean isLast() {
			if(selected.length==0) {
				return false;
			}
			if(cursor == selected.length-1) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isFirst() {
			if(selected.length==0) {
				return false;
			}
			if(cursor == 0) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isAfterLast() {
			if(selected.length==0) {
				return false;
			}
			if(cursor == selected.length) {
				return true;
			}
			return false;
		}

		@Override
		public boolean isBeforeFirst() {
			if(selected.length==0) {
				return false;
			}
			if(cursor == -1) {
				return true;
			}
			return false;
		}

		@Override
		public Object getObject(int col) {
			// TODO Auto-generated method stub
			return selected[cursor][col-1];
		}

		@Override
		public void afterLast() {
			// TODO Auto-generated method stub
			cursor = selected.length;
		}

		@Override
		public void beforeFirst() {
			cursor = -1;
		}
		
		@Override
		public Object [][] getSelected(){
			return selected;	
		}

		@Override
		public boolean absolute(int row) {
			if(selected.length==0) {
				return false;
			}
			if(row<selected.length&&row>0) {
				cursor = row;
				return true;
			}else if(row==-1) {
				cursor = selected.length-1;
				return true;
			}else if(row==-2) {
				cursor = selected.length; // true???
				return true;
			}
			return false;
		}
		
	}
	
	

}
