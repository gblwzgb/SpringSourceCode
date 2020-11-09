/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.core.type.classreading;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.asm.AnnotationVisitor;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.core.type.MethodMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * ASM class visitor that creates {@link SimpleAnnotationMetadata}.
 *
 * @author Phillip Webb
 * @since 5.2
 */
// 实现回调方法(visit)，asm框架会按固定的顺序调用这些visit，该visitor实现负责提供回调，并将读到的class信息记录下来，在visitEnd中，将记录下来的一系列信息，打包成SimpleAnnotationMetadata
final class SimpleAnnotationMetadataReadingVisitor extends ClassVisitor {

	@Nullable
	private final ClassLoader classLoader;

	private String className = "";

	private int access;

	@Nullable
	private String superClassName;

	private String[] interfaceNames = new String[0];

	@Nullable
	private String enclosingClassName;

	private boolean independentInnerClass;

	private Set<String> memberClassNames = new LinkedHashSet<>(4);

	private List<MergedAnnotation<?>> annotations = new ArrayList<>();

	private List<SimpleMethodMetadata> annotatedMethods = new ArrayList<>();

	@Nullable
	private SimpleAnnotationMetadata metadata;

	@Nullable
	private Source source;


	SimpleAnnotationMetadataReadingVisitor(@Nullable ClassLoader classLoader) {
		super(SpringAsmInfo.ASM_VERSION);
		this.classLoader = classLoader;
	}


	@Override
	public void visit(int version, int access, String name, String signature,
			@Nullable String supername, String[] interfaces) {

		// 资源名 -> 全限定名
		this.className = toClassName(name);
		// 访问权限
		this.access = access;
		if (supername != null && !isInterface(access)) {
			// 超类
			this.superClassName = toClassName(supername);
		}
		// 一组接口名
		this.interfaceNames = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			this.interfaceNames[i] = toClassName(interfaces[i]);
		}
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		this.enclosingClassName = toClassName(owner);
	}

	@Override
	public void visitInnerClass(String name, @Nullable String outerName, String innerName,
			int access) {
		if (outerName != null) {
			String className = toClassName(name);
			String outerClassName = toClassName(outerName);
			if (this.className.equals(className)) {
				this.enclosingClassName = outerClassName;
				this.independentInnerClass = ((access & Opcodes.ACC_STATIC) != 0);
			}
			else if (this.className.equals(outerClassName)) {
				this.memberClassNames.add(className);
			}
		}
	}

	@Override
	@Nullable
	public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
		return MergedAnnotationReadingVisitor.get(this.classLoader, this::getSource,
				descriptor, visible, this.annotations::add);
	}

	@Override
	@Nullable
	public MethodVisitor visitMethod(
			int access, String name, String descriptor, String signature, String[] exceptions) {

		// Skip bridge methods - we're only interested in original
		// annotation-defining user methods. On JDK 8, we'd otherwise run into
		// double detection of the same annotated method...
		if (isBridge(access)) {
			return null;
		}
		return new SimpleMethodMetadataReadingVisitor(this.classLoader, this.className,
				access, name, descriptor, this.annotatedMethods::add);
	}

	@Override
	public void visitEnd() {
		// 成员类的类名
		String[] memberClassNames = StringUtils.toStringArray(this.memberClassNames);
		// 被注解了的方法的元数据
		// PS：这里数组传的是0，但是内部会自动扩容的，并不是只取一个的意思。。。如果用list.toArray()的话，返回的Object的，需要强转，挺麻烦的。
		MethodMetadata[] annotatedMethods = this.annotatedMethods.toArray(new MethodMetadata[0]);
		// 合并过的注解
		MergedAnnotations annotations = MergedAnnotations.of(this.annotations);
		// 封装成 SimpleAnnotationMetadata
		this.metadata = new SimpleAnnotationMetadata(this.className, this.access,
				this.enclosingClassName, this.superClassName, this.independentInnerClass,
				this.interfaceNames, memberClassNames, annotatedMethods, annotations);
	}

	public SimpleAnnotationMetadata getMetadata() {
		Assert.state(this.metadata != null, "AnnotationMetadata not initialized");
		return this.metadata;
	}

	private Source getSource() {
		Source source = this.source;
		if (source == null) {
			source = new Source(this.className);
			this.source = source;
		}
		return source;
	}

	private String toClassName(String name) {
		return ClassUtils.convertResourcePathToClassName(name);
	}

	private boolean isBridge(int access) {
		return (access & Opcodes.ACC_BRIDGE) != 0;
	}

	private boolean isInterface(int access) {
		return (access & Opcodes.ACC_INTERFACE) != 0;
	}

	/**
	 * {@link MergedAnnotation} source.
	 */
	private static final class Source {

		private final String className;

		Source(String className) {
			this.className = className;
		}

		@Override
		public int hashCode() {
			return this.className.hashCode();
		}

		@Override
		public boolean equals(@Nullable Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}
			return this.className.equals(((Source) obj).className);
		}

		@Override
		public String toString() {
			return this.className;
		}

	}

}
