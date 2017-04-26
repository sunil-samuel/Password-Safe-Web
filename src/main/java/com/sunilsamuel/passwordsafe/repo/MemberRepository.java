/**
 * MemberRepository.java (Sep 17, 2014 - 11:30:10 PM)
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

import org.springframework.data.jpa.repository.JpaRepository;

import com.sunilsamuel.passwordsafe.model.Member;
import com.sunilsamuel.passwordsafe.model.Role;

//http://docs.spring.io/spring-data/data-commons/docs/current/reference/html/
public interface MemberRepository extends JpaRepository<Member, Long> {
	List<Member> findByEmailIgnoreCaseAndPassword(String email, String password);

	List<Member> findAllByOrderByLastNameAsc();

	List<Member> findByRole(Role role);

	Member findOneByIdAndEmail(Long id, String email);

	Long deleteByIdAndEmail(Long id, String email);

	Long countByRole(Role role);

}
