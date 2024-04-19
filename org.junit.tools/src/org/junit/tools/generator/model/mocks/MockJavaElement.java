package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.JavaModelException;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonIncludeProperties({ "elementName", "ancestor" })
public class MockJavaElement implements IJavaElement {

    private MockJavaElement ancestor;
    private String elementName;

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getAncestor(int arg0) {
	return ancestor;
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor arg0) throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getCorrespondingResource() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getElementName() {
	return elementName;
    }

    @Override
    public int getElementType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getHandleIdentifier() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaModel getJavaModel() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaProject getJavaProject() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOpenable getOpenable() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getParent() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getPath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getPrimaryElement() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getResource() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ISchedulingRule getSchedulingRule() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getUnderlyingResource() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isReadOnly() {
	return false;
    }

    @Override
    public boolean isStructureKnown() throws JavaModelException {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
