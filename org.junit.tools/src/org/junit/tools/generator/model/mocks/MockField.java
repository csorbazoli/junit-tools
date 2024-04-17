package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockField implements IField {

    private final List<MockAnnotation> annotations = new LinkedList<>();
    private String typeSignature;
    private String elementName;
    private int flags;
    private MockJavaElement ancestor;
    private String source;
    private boolean exists;

    @Override
    public String[] getCategories() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile getClassFile() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getCompilationUnit() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType getDeclaringType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getFlags() {
	return flags;
    }

    @Override
    public ISourceRange getJavadocRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getOccurrenceCount() {
	return 0;
    }

    @Override
    public IType getType(String arg0, int arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeRoot getTypeRoot() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isBinary() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists() {
	return exists;
    }

    @Override
    public IJavaElement getAncestor(int arg0) {
	return ancestor;
    }

    @Override
    public String getAttachedJavadoc(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IResource getCorrespondingResource() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
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
    public IResource getUnderlyingResource() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isReadOnly() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isStructureKnown() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ISourceRange getNameRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSource() {
	return source;
    }

    @Override
    public ISourceRange getSourceRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) {

    }

    @Override
    public void delete(boolean arg0, IProgressMonitor arg1) {

    }

    @Override
    public void move(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) {

    }

    @Override
    public void rename(String arg0, boolean arg1, IProgressMonitor arg2) {

    }

    @Override
    public IJavaElement[] getChildren() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasChildren() {
	return false;
    }

    @Override
    public IAnnotation getAnnotation(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IAnnotation[] getAnnotations() {
	return annotations.toArray(new IAnnotation[0]);
    }

    @Override
    public Object getConstant() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getElementName() {
	return elementName;
    }

    @Override
    public String getKey() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getTypeSignature() {
	return typeSignature;
    }

    @Override
    public boolean isEnumConstant() {
	return false;
    }

    @Override
    public boolean isRecordComponent() {
	return false;
    }

    @Override
    public boolean isResolved() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
