/**
 * CategoryRepository.java (Sep 17, 2014 - 11:30:10 PM)
 *
 * Sunil Samuel CONFIDENTIAL
 *
 *  [2017] Sunil Samuel
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Sunil Samuel. The intellectual and technical
 * concepts contained herein are proprietary to Sunil Samuel
 * and may be covered by U.S. and Foreign Patents, patents in
 * process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written permission
 * is obtained from Sunil Samuel.
 */

package com.sunilsamuel.passwordsafe.repo;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sunilsamuel.passwordsafe.model.Category;

//http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/
public interface CategoryRepository extends JpaRepository<Category, Long> {
	Long countByParentId(Long parentId);

	List<Category> findByParentId(Long parentId);

	List<Category> findByTitle(String title);

	List<Category> findByTitleAndParentId(String title, Long parentId);

	List<Category> findAllByOrderByTitleAsc();

	List<Category> findByParentId(Long parentId, Sort sort);
}