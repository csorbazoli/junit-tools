package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.assertj.core.util.Arrays;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IBufferFactory;
import org.eclipse.jdt.core.ICodeCompletionRequestor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IImportContainer;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockCompilationUnit implements ICompilationUnit {

    private String elementName;
    private IJavaProject javaProject;
    private boolean readOnly;
    private IType[] baseTypes;

    @Override
    public IType findPrimaryType() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getElementAt(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getWorkingCopy(WorkingCopyOwner arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean exists() {
	return true;
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

    private IJavaElement primaryElement;

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
    public boolean isStructureKnown() {
	return true;
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private IJavaElement[] children;

    @Override
    public boolean hasChildren() {
	return !Arrays.isNullOrEmpty(children);
    }

    @Override
    public void close() {
	// nothing to do
    }

    @Override
    public String findRecommendedLineSeparator() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IBuffer getBuffer() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasUnsavedChanges() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isConsistent() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isOpen() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void makeConsistent(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void open(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void save(IProgressMonitor arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ISourceRange getNameRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSource() {
	StringBuilder ret = new StringBuilder();
	return ret.toString();
    }

    @Override
    public ISourceRange getSourceRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, ICodeCompletionRequestor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, ICompletionRequestor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, CompletionRequestor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, CompletionRequestor arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, ICompletionRequestor arg1, WorkingCopyOwner arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, CompletionRequestor arg1, WorkingCopyOwner arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void codeComplete(int arg0, CompletionRequestor arg1, WorkingCopyOwner arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement[] codeSelect(int arg0, int arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement[] codeSelect(int arg0, int arg1, WorkingCopyOwner arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void commit(boolean arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void destroy() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement findSharedWorkingCopy(IBufferFactory arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getOriginal(IJavaElement arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getOriginalElement() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getSharedWorkingCopy(IProgressMonitor arg0, IBufferFactory arg1, IProblemRequestor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getWorkingCopy() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement getWorkingCopy(IProgressMonitor arg0, IBufferFactory arg1, IProblemRequestor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isBasedOn(IResource arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IMarker[] reconcile() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void reconcile(boolean arg0, IProgressMonitor arg1) {
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
    public UndoEdit applyTextEdit(TextEdit arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void becomeWorkingCopy(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void becomeWorkingCopy(IProblemRequestor arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void commitWorkingCopy(boolean arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IImportDeclaration createImport(String arg0, IJavaElement arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IImportDeclaration createImport(String arg0, IJavaElement arg1, int arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageDeclaration createPackageDeclaration(String arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType createType(String arg0, IJavaElement arg1, boolean arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void discardWorkingCopy() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement[] findElements(IJavaElement arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit findWorkingCopy(WorkingCopyOwner arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType[] getAllTypes() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IImportDeclaration getImport(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IImportContainer getImportContainer() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IImportDeclaration[] getImports() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public WorkingCopyOwner getOwner() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageDeclaration getPackageDeclaration(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageDeclaration[] getPackageDeclarations() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getPrimary() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType getType(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType[] getTypes() {
	return baseTypes;
    }

    @Override
    public ICompilationUnit getWorkingCopy(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getWorkingCopy(WorkingCopyOwner arg0, IProblemRequestor arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasResourceChanged() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isWorkingCopy() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public CompilationUnit reconcile(int arg0, boolean arg1, WorkingCopyOwner arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public CompilationUnit reconcile(int arg0, int arg1, WorkingCopyOwner arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public CompilationUnit reconcile(int arg0, boolean arg1, boolean arg2, WorkingCopyOwner arg3, IProgressMonitor arg4) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void restore() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
