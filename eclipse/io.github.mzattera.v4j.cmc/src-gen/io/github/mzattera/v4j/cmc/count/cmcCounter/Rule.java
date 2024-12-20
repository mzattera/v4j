/* Copyright (c) 2022 Massimiliano "Maxi" Zattera */

/**
 * generated by Xtext 2.28.0
 */
package io.github.mzattera.v4j.cmc.count.cmcCounter;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link io.github.mzattera.v4j.cmc.count.cmcCounter.Rule#getName <em>Name</em>}</li>
 *   <li>{@link io.github.mzattera.v4j.cmc.count.cmcCounter.Rule#getExpansions <em>Expansions</em>}</li>
 * </ul>
 *
 * @see io.github.mzattera.v4j.cmc.count.cmcCounter.CmcCounterPackage#getRule()
 * @model
 * @generated
 */
public interface Rule extends EObject
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see io.github.mzattera.v4j.cmc.count.cmcCounter.CmcCounterPackage#getRule_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link io.github.mzattera.v4j.cmc.count.cmcCounter.Rule#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Expansions</b></em>' containment reference list.
   * The list contents are of type {@link io.github.mzattera.v4j.cmc.count.cmcCounter.Expansion}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expansions</em>' containment reference list.
   * @see io.github.mzattera.v4j.cmc.count.cmcCounter.CmcCounterPackage#getRule_Expansions()
   * @model containment="true"
   * @generated
   */
  EList<Expansion> getExpansions();

} // Rule
