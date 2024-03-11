package org.junit.tools.generator.model.mocks;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.eclipse.jdt.core.IJavaElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MockCompilationUnitTest {

    @InjectMocks
    MockCompilationUnit underTest;

    @Test
    public void testGetSource_should() throws Exception {
	// given
	// when
	String actual = underTest.getSource();
	// then
	assertEquals("", actual);
    }

    @Test
    public void testExists_shouldReturnTrue() throws Exception {
	// given
	// when
	boolean actual = underTest.exists();
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testIsReadOnly_shouldreturnFalse() throws Exception {
	// given
	// when
	boolean actual = underTest.isReadOnly();
	// then
	assertThat(actual).isFalse();
    }

    @Test
    public void testIsStructureKnown_shouldReturnTrue() throws Exception {
	// given
	// when
	boolean actual = underTest.isStructureKnown();
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testHasChildren_shouldTrueIfChilrenIsNotEmpty() throws Exception {
	// given
	IJavaElement element = mock(IJavaElement.class);
	underTest.setChildren(new IJavaElement[] { element });
	// when
	boolean actual = underTest.hasChildren();
	// then
	assertThat(actual).isTrue();
    }

    @Test
    public void testClose_shouldDoNothing() throws Exception {
	// given
	// when
	underTest.close();
	// then
	assertThat(underTest).isNotNull();
    }
}