package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IModularClassFile;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IOrdinaryClassFile;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.WorkingCopyOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MockPackageFragment implements IPackageFragment {

    private MockCompilationUnit parent;

    @Override
    public IJavaElement[] getChildren() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasChildren() {
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

    private int elementType;

    private String handleIdentifier;

    private IJavaModel javaModel;

    private IJavaProject javaProject;

    private IOpenable openable;

    private IPath path;

    private IJavaElement primaryElement;

    private IResource resource;

    private ISchedulingRule schedulingRule;

    @Override
    public IResource getUnderlyingResource() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private boolean isReadOnly;

    @Override
    public boolean isStructureKnown() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public <T> T getAdapter(Class<T> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void close() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
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

    private boolean open;

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
    public boolean containsJavaResources() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit createCompilationUnit(String fileName, String arg1, boolean arg2, IProgressMonitor arg3) {
	return MockCompilationUnit.builder()
		.elementName(fileName)
		.build();
    }

    @Override
    public IClassFile[] getAllClassFiles() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile getClassFile(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClassFile[] getClassFiles() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit getCompilationUnit(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit[] getCompilationUnits() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ICompilationUnit[] getCompilationUnits(WorkingCopyOwner arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private String elementName;

    @Override
    public int getKind() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private IModularClassFile modularClassFile;

    @Override
    public Object[] getNonJavaResources() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOrdinaryClassFile getOrdinaryClassFile(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IOrdinaryClassFile[] getOrdinaryClassFiles() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasSubpackages() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    private boolean defaultPackage;

}
