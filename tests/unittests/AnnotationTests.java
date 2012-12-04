package unittests;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import api.contentRetrival.impl.results.Annotation;
import api.contentRetrival.interfaces.IAnnotation;

public class AnnotationTests {
	
	private static IAnnotation ann;
	
	@Before
	public void init() { 
		ann = new Annotation("name", "text","id");
	}
	
	
	@Test
	public void testGetName() {
		String expected = "name";
		assertEquals(expected, ann.getUserName());

	}
	
	@Test
	public void testGetText() {
		String expected = "text";
		assertEquals(expected, ann.getText());

	}
	
	@Test
	public void testGetId() {
		String expected = "id";
		assertEquals(expected, ann.getUID());

	}



}
