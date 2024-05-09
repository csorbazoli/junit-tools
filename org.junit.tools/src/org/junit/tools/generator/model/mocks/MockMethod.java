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
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "elementName", "flags", "exists", "isConstructor", "source", "parameterTypes" })
public class MockMethod implements IMethod {

    private boolean exists;
    private int flags;
    private String elementName;
    private boolean isConstructor;
    private String source;
    private String[] parameterTypes;
    @Builder.Default
    private List<MockAnnotation> annotations = new LinkedList<>();
    private MockCompilationUnit compilationUnit;

    public MockMethod addAnnotation(MockAnnotation annotation) {
	annotations.add(annotation);
	return this;
    }

    @Override
    public String[] getCategories() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile getClassFile() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public MockCompilationUnit getCompilationUnit() {
	return compilationUnit;
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
	throw new IllegalStateException(NOT_IMPLEMENTED);
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
    public IJavaElement getAncestor(int ancestorType) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
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
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IAnnotation getAnnotation(String name) {
	return annotations.stream()
		.filter(annotation -> annotation.getElementName().startsWith(name))
		.findFirst()
		.orElseGet(() -> MockAnnotation.builder()
			.elementName(name)
			.exists(false)
			.build());
    }

    @Override
    public MockAnnotation[] getAnnotations() {
	return annotations.toArray(new MockAnnotation[0]);
    }

    @Override
    public IMemberValuePair getDefaultValue() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getElementName() {
	return elementName;
    }

    @Override
    public String[] getExceptionTypes() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getKey() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public int getNumberOfParameters() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getParameterNames() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getParameterTypes() {
	return parameterTypes;
    }

    @Override
    public ILocalVariable[] getParameters() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getRawParameterNames() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getReturnType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSignature() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeParameter getTypeParameter(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getTypeParameterSignatures() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeParameter[] getTypeParameters() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isConstructor() {
	return isConstructor;
    }

    @Override
    public boolean isLambdaMethod() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isMainMethod() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isResolved() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isSimilar(IMethod arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
