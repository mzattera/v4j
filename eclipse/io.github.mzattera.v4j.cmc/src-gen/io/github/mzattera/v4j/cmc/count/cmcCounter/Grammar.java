/**
 * generated by Xtext 2.25.0
 */
package io.github.mzattera.v4j.cmc.count.cmcCounter;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Grammar</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link io.github.mzattera.v4j.cmc.count.cmcCounter.Grammar#getRules <em>Rules</em>}</li>
 * </ul>
 *
 * @see io.github.mzattera.v4j.cmc.count.cmcCounter.CmcCounterPackage#getGrammar()
 * @model
 * @generated
 */
public interface Grammar extends EObject
{
  /**
   * Returns the value of the '<em><b>Rules</b></em>' containment reference list.
   * The list contents are of type {@link io.github.mzattera.v4j.cmc.count.cmcCounter.Rule}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Rules</em>' containment reference list.
   * @see io.github.mzattera.v4j.cmc.count.cmcCounter.CmcCounterPackage#getGrammar_Rules()
   * @model containment="true"
   * @generated
   */
  EList<Rule> getRules();

} // Grammar