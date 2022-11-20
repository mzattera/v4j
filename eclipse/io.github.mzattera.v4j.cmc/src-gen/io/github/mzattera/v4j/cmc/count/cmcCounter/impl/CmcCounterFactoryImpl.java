/**
 * generated by Xtext 2.28.0
 */
package io.github.mzattera.v4j.cmc.count.cmcCounter.impl;

import io.github.mzattera.v4j.cmc.count.cmcCounter.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CmcCounterFactoryImpl extends EFactoryImpl implements CmcCounterFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static CmcCounterFactory init()
  {
    try
    {
      CmcCounterFactory theCmcCounterFactory = (CmcCounterFactory)EPackage.Registry.INSTANCE.getEFactory(CmcCounterPackage.eNS_URI);
      if (theCmcCounterFactory != null)
      {
        return theCmcCounterFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new CmcCounterFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public CmcCounterFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case CmcCounterPackage.GRAMMAR: return createGrammar();
      case CmcCounterPackage.RULE_OR_TEXT: return createRuleOrText();
      case CmcCounterPackage.RULE: return createRule();
      case CmcCounterPackage.EXPANSION: return createExpansion();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Grammar createGrammar()
  {
    GrammarImpl grammar = new GrammarImpl();
    return grammar;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public RuleOrText createRuleOrText()
  {
    RuleOrTextImpl ruleOrText = new RuleOrTextImpl();
    return ruleOrText;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Rule createRule()
  {
    RuleImpl rule = new RuleImpl();
    return rule;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Expansion createExpansion()
  {
    ExpansionImpl expansion = new ExpansionImpl();
    return expansion;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public CmcCounterPackage getCmcCounterPackage()
  {
    return (CmcCounterPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static CmcCounterPackage getPackage()
  {
    return CmcCounterPackage.eINSTANCE;
  }

} //CmcCounterFactoryImpl
