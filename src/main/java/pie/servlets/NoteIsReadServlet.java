package pie.servlets;

import javax.servlet.http.HttpServlet;

import pie.services.NoteService;

import com.google.inject.Singleton;

@Singleton
public class NoteIsReadServlet extends HttpServlet {

	private static final long serialVersionUID = 6267658636515588161L;

	NoteService noteService;
	
	public NoteIsReadServlet(NoteService noteService) {
		this.noteService = noteService;
	}
	
}
