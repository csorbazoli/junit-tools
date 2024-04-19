package org.junit.tools.generator.model.mocks;

import static org.junit.tools.generator.model.mocks.MockConstants.NOT_IMPLEMENTED;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IModuleDescription;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IRegion;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.WorkingCopyOwner;
import org.eclipse.jdt.core.eval.IEvaluationContext;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIncludeProperties({ "elementName", "project", "packageFragmentRoot"
})
public class MockJavaProject implements IJavaProject {

    private String elementName;
    private MockProject project;
    private MockPackageFragmentRoot packageFragmentRoot;

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
    public IClasspathEntry decodeClasspathEntry(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Set<String> determineModulesOfProjectsWithNonEmptyClasspath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String encodeClasspathEntry(IClasspathEntry arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry findContainingClasspathEntry(IResource arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement findElement(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement findElement(IPath arg0, WorkingCopyOwner arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IJavaElement findElement(String arg0, WorkingCopyOwner arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IModuleDescription findModule(String arg0, WorkingCopyOwner arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragment findPackageFragment(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot findPackageFragmentRoot(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot[] findPackageFragmentRoots(IClasspathEntry arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, WorkingCopyOwner arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, String arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, WorkingCopyOwner arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, String arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, String arg1, WorkingCopyOwner arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IType findType(String arg0, String arg1, WorkingCopyOwner arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot[] findUnfilteredPackageFragmentRoots(IClasspathEntry arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot[] getAllPackageFragmentRoots() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry getClasspathEntryFor(IPath arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IModuleDescription getModuleDescription() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Object[] getNonJavaResources() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String getOption(String arg0, boolean arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public Map<String, String> getOptions(boolean arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath getOutputLocation() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IModuleDescription getOwnModuleDescription() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot getPackageFragmentRoot(String arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot getPackageFragmentRoot(IResource arg0) {
	return packageFragmentRoot;
    }

    @Override
    public IPackageFragmentRoot[] getPackageFragmentRoots() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragmentRoot[] getPackageFragmentRoots(IClasspathEntry arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPackageFragment[] getPackageFragments() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry[] getRawClasspath() {
	return new IClasspathEntry[] {
		MockClasspathEntry.builder()
			.path(this.packageFragmentRoot.getPath())
			.build()
	};
    }

    @Override
    public IClasspathEntry[] getReferencedClasspathEntries() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public String[] getRequiredProjectNames() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry[] getResolvedClasspath(boolean arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasBuildState() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean hasClasspathCycle(IClasspathEntry[] arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isOnClasspath(IJavaElement arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public boolean isOnClasspath(IResource arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IEvaluationContext newEvaluationContext() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IRegion arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IRegion arg0, WorkingCopyOwner arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IType arg0, IRegion arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public ITypeHierarchy newTypeHierarchy(IType arg0, IRegion arg1, WorkingCopyOwner arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IPath readOutputLocation() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public IClasspathEntry[] readRawClasspath() {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setOption(String arg0, String arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setOptions(Map<String, String> arg0) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setOutputLocation(IPath arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRawClasspath(IClasspathEntry[] arg0, IProgressMonitor arg1) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRawClasspath(IClasspathEntry[] arg0, boolean arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRawClasspath(IClasspathEntry[] arg0, IPath arg1, IProgressMonitor arg2) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRawClasspath(IClasspathEntry[] arg0, IPath arg1, boolean arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

    @Override
    public void setRawClasspath(IClasspathEntry[] arg0, IClasspathEntry[] arg1, IPath arg2, IProgressMonitor arg3) {
	throw new IllegalStateException(NOT_IMPLEMENTED);
    }

}
