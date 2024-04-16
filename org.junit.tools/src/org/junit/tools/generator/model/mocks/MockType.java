package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.WorkingCopyOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockType implements IType {

    private String elementName;
    private IAnnotation[] annotations;

    @Override
    public String[] getCategories() {
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
	throw new IllegalStateException(NOT_IMPLEMENTED);
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
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getAncestor(int arg0) {
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
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ISourceRange getSourceRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(boolean arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void move(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void rename(String arg0, boolean arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
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
    public IAnnotation getAnnotation(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IAnnotation[] getAnnotations() {
	return annotations;
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, ICompletionRequestor arg7) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, ICompletionRequestor arg7,
	    WorkingCopyOwner arg8) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
	    IProgressMonitor arg8) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
	    WorkingCopyOwner arg8) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(char[] arg0, int arg1, int arg2, char[][] arg3, char[][] arg4, int[] arg5, boolean arg6, CompletionRequestor arg7,
	    WorkingCopyOwner arg8, IProgressMonitor arg9) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IField createField(String arg0, IJavaElement arg1, boolean arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IInitializer createInitializer(String arg0, IJavaElement arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMethod createMethod(String arg0, IJavaElement arg1, boolean arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType createType(String arg0, IJavaElement arg1, boolean arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMethod[] findMethods(IMethod arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement[] getChildrenForCategory(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOrdinaryClassFile getClassFile() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getElementName() {
	return elementName;
    }

    @Override
    public IField getField(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IField[] getFields() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getFullyQualifiedName() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getFullyQualifiedName(char arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getFullyQualifiedParameterizedName() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IInitializer getInitializer(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IInitializer[] getInitializers() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getKey() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMethod getMethod(String arg0, String[] arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMethod[] getMethods() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragment getPackageFragment() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getPermittedSubtypeNames() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IField getRecordComponent(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getSuperInterfaceNames() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getSuperInterfaceTypeSignatures() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSuperclassName() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSuperclassTypeSignature() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType getType(String arg0) {
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
    public String getTypeQualifiedName() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getTypeQualifiedName(char arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType[] getTypes() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isAnnotation() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isAnonymous() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isClass() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isEnum() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isInterface() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isLambda() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isLocal() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isMember() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isRecord() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isResolved() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isSealed() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy loadTypeHierachy(InputStream arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy(ICompilationUnit[] arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy(IWorkingCopy[] arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newSupertypeHierarchy(WorkingCopyOwner arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IJavaProject arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(ICompilationUnit[] arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IWorkingCopy[] arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(WorkingCopyOwner arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IJavaProject arg0, WorkingCopyOwner arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[][] resolveType(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[][] resolveType(String arg0, WorkingCopyOwner arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
