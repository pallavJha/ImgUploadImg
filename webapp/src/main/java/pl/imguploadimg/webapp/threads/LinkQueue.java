package pl.imguploadimg.webapp.threads;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class LinkQueue {

	class Link {

		String link;
		Link next;

		public Link() {
			this.link = null;
			next = null;
		}

		public Link(String link) {
			this.link = link;
			next = null;
		}

		@Override
		public String toString() {
			return link;
		}
	}

	private Link head, tail, next;

	private long size = 0L;

	public LinkQueue(List<String> linkSet) {
		if (linkSet != null && linkSet.size() > 0) {
			LinkedList<String> list = new LinkedList<String>(linkSet);
			Iterator<String> listIterator = list.iterator();
			while (listIterator.hasNext()) {
				Link newLink = new Link(listIterator.next());
				enQueue(newLink);
			}
		}
	}

	public void enQueue(String link) {
		Link newLink = new Link(link);
		enQueue(newLink);
	}

	private void enQueue(Link newLink) {
		if (head == null) {
			head = newLink;
			tail = newLink;
		} else {
			tail.next = newLink;
			tail = newLink;
		}
		size++;
	}

	public String deQueue() {
		if (head == null) {
			return null;
		} else {
			Link temp = head;
			head = head.next;
			size--;
			return temp.link;
		}
	}

	public long size() {
		return size;
	}
	
	public boolean isEmpty(){
		return head == null;
	}
	
	public String next(){
		if(next == null){
			next = head;
			return next.link;
		}
		next = next.next;
		if(next != null){
			return next.link;
		}
		else{
			return null;
		}
	}
}
