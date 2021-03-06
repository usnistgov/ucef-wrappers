/**
 */
package gov.nist.pages.ucef.util;

import gov.nist.pages.ucef.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see gov.nist.pages.ucef.ucefPackage
 * @generated
 */
public class ucefSwitch<T> extends Switch<T> {
    /**
     * The cached model package
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected static ucefPackage modelPackage;

    /**
     * Creates an instance of the switch.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public ucefSwitch() {
        if (modelPackage == null) {
            modelPackage = ucefPackage.eINSTANCE;
        }
    }

    /**
     * Checks whether this is a switch for the given package.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param ePackage the package in question.
     * @return whether this is a switch for the given package.
     * @generated
     */
    @Override
    protected boolean isSwitchFor(EPackage ePackage) {
        return ePackage == modelPackage;
    }

    /**
     * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the first non-null result returned by a <code>caseXXX</code> call.
     * @generated
     */
    @Override
    protected T doSwitch(int classifierID, EObject theEObject) {
        switch (classifierID) {
            case ucefPackage.ATTRIBUTE_DETAILS_TYPE: {
                AttributeDetailsType attributeDetailsType = (AttributeDetailsType)theEObject;
                T result = caseAttributeDetailsType(attributeDetailsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.DOCUMENT_ROOT: {
                DocumentRoot documentRoot = (DocumentRoot)theEObject;
                T result = caseDocumentRoot(documentRoot);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.IGNORED_TYPE: {
                IgnoredType ignoredType = (IgnoredType)theEObject;
                T result = caseIgnoredType(ignoredType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.INTERACTION_DETAILS_TYPE: {
                InteractionDetailsType interactionDetailsType = (InteractionDetailsType)theEObject;
                T result = caseInteractionDetailsType(interactionDetailsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.LINEAR_CONVERSION_TYPE: {
                LinearConversionType linearConversionType = (LinearConversionType)theEObject;
                T result = caseLinearConversionType(linearConversionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.OBJECT_DETAILS_TYPE: {
                ObjectDetailsType objectDetailsType = (ObjectDetailsType)theEObject;
                T result = caseObjectDetailsType(objectDetailsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.PARAMETER_DETAILS_TYPE: {
                ParameterDetailsType parameterDetailsType = (ParameterDetailsType)theEObject;
                T result = caseParameterDetailsType(parameterDetailsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.PUBLISHED_OBJECTS_TYPE: {
                PublishedObjectsType publishedObjectsType = (PublishedObjectsType)theEObject;
                T result = casePublishedObjectsType(publishedObjectsType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            case ucefPackage.UNIT_CONVERSION_TYPE: {
                UnitConversionType unitConversionType = (UnitConversionType)theEObject;
                T result = caseUnitConversionType(unitConversionType);
                if (result == null) result = defaultCase(theEObject);
                return result;
            }
            default: return defaultCase(theEObject);
        }
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Attribute Details Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Attribute Details Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseAttributeDetailsType(AttributeDetailsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Document Root</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseDocumentRoot(DocumentRoot object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Ignored Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Ignored Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseIgnoredType(IgnoredType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Interaction Details Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Interaction Details Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseInteractionDetailsType(InteractionDetailsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Linear Conversion Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Linear Conversion Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseLinearConversionType(LinearConversionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Object Details Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Object Details Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseObjectDetailsType(ObjectDetailsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Parameter Details Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Parameter Details Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseParameterDetailsType(ParameterDetailsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Published Objects Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Published Objects Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T casePublishedObjectsType(PublishedObjectsType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>Unit Conversion Type</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>Unit Conversion Type</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
     * @generated
     */
    public T caseUnitConversionType(UnitConversionType object) {
        return null;
    }

    /**
     * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
     * <!-- begin-user-doc -->
     * This implementation returns null;
     * returning a non-null result will terminate the switch, but this is the last case anyway.
     * <!-- end-user-doc -->
     * @param object the target of the switch.
     * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
     * @see #doSwitch(org.eclipse.emf.ecore.EObject)
     * @generated
     */
    @Override
    public T defaultCase(EObject object) {
        return null;
    }

} //ucefSwitch
