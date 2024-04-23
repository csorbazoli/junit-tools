package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IProblemRequestor;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.text.edits.UndoEdit;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "elementName", "javaProject", "readOnly", "baseTypes", "childElements", "importDeclarations", "packageDeclarations", "primaryElement",
	"changed" })
public class MockCompilationUnit implements ICompilationUnit {

    private String elementName;
    private MockJavaProject javaProject;
    private boolean readOnly;
    private final List<MockType> baseTypes = new LinkedList<>();
    private final List<MockJavaElement> childElements = new LinkedList<>();
    private final List<MockImportDeclaration> importDeclarations = new LinkedList<>();
    private final List<MockPackageDeclaration> packageDeclarations = new LinkedList<>();
    private MockJavaElement primaryElement;
    @Builder.Default
    private boolean changed = true;

    @Override
    public MockType findPrimaryType() {
	return baseTypes.isEmpty() ? null : baseTypes.get(0);
    }

    @Override
    public IJavaElement getElementAt(int arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getWorkingCopy(WorkingCopyOwner arg0, IProgressMonitor monitor) {
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

    @Override
    public IJavaElement[] getChildren() throws JavaModelException {
	return childElements.toArray(new IJavaElement[0]);
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
    public boolean isStructureKnown() {
	return true;
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasChildren() {
	return !childElements.isEmpty();
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
	return changed;
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
	// nothing to do
    }

    @Override
    public void open(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void save(IProgressMonitor arg0, boolean arg1) {
	// nothing to do
    }

    @Override
    public ISourceRange getNameRange() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getSource() {
	return CompilationUnitSourceConverter.toJavaSource(this);
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
    public void commit(boolean arg0, IProgressMonitor monitor) {
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
    public void reconcile(boolean arg0, IProgressMonitor monitor) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void copy(IJavaElement arg0, IJavaElement arg1, String arg2, boolean arg3, IProgressMonitor arg4) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void delete(boolean arg0, IProgressMonitor monitor) {
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
    public UndoEdit applyTextEdit(TextEdit arg0, IProgressMonitor monitor) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void becomeWorkingCopy(IProgressMonitor arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void becomeWorkingCopy(IProblemRequestor arg0, IProgressMonitor monitor) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void commitWorkingCopy(boolean arg0, IProgressMonitor monitor) {
	changed = false;
    }

    @Override
    public IImportDeclaration createImport(String name, IJavaElement sibling, IProgressMonitor monitor) {
	MockImportDeclaration ret = MockImportDeclaration.builder()
		.elementName(name)
		.ancestor((MockJavaElement) sibling)
		.build();
	importDeclarations.add(ret);
	return ret;
    }

    @Override
    public IImportDeclaration createImport(String name, IJavaElement sibling, int flags, IProgressMonitor monitor) {
	MockImportDeclaration ret = MockImportDeclaration.builder()
		.elementName(name)
		.ancestor((MockJavaElement) sibling)
		.flags(flags)
		.build();
	importDeclarations.add(ret);
	return ret;
    }

    @Override
    public IPackageDeclaration createPackageDeclaration(String name, IProgressMonitor monitor) {
	MockPackageDeclaration ret = MockPackageDeclaration.builder()
		.elementName(name)
		.build();
	packageDeclarations.add(ret);
	return ret;
    }

    @Override
    public IType createType(String contents, IJavaElement sibling, boolean force, IProgressMonitor monitor) {
	MockType ret = findPrimaryType();
	if (ret == null) {
	    MockType.builder()
		    .source(contents)
		    .build();
	    baseTypes.add(ret);
	} else {
	    ret.setSource(contents);
	}
	return ret;
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
	return importDeclarations.toArray(new IImportDeclaration[0]);
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
	return packageDeclarations.toArray(new IPackageDeclaration[0]);
    }

    @Override
    public ICompilationUnit getPrimary() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    /**
     * Returns the top-level type declared in this compilation unit with the given
     * simple type name.
     */
    public IType getType(String name) {
	Optional<MockType> ret = baseTypes.stream()
		.filter(t -> name.equals(t.getElementName()))
		.findFirst();
	if (!ret.isPresent()) {
	    ret = Optional.of(MockType.builder()
		    .elementName(name)
		    .build());
	    baseTypes.add(ret.get());
	}
	return ret.get();
    }

    @Override
    public IType[] getTypes() {
	return baseTypes.toArray(new IType[0]);
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
	return changed;
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
