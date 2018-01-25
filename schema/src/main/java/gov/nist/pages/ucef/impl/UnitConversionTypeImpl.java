/**
 */
package gov.nist.pages.ucef.impl;

import gov.nist.pages.ucef.LinearConversionType;
import gov.nist.pages.ucef.UnitConversionType;
import gov.nist.pages.ucef.ucefPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Conversion Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link gov.nist.pages.ucef.impl.UnitConversionTypeImpl#getNameConversion <em>Name Conversion</em>}</li>
 *   <li>{@link gov.nist.pages.ucef.impl.UnitConversionTypeImpl#getLinearConversion <em>Linear Conversion</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UnitConversionTypeImpl extends MinimalEObjectImpl.Container implements UnitConversionType {
    /**
     * The default value of the '{@link #getNameConversion() <em>Name Conversion</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNameConversion()
     * @generated
     * @ordered
     */
    protected static final String NAME_CONVERSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getNameConversion() <em>Name Conversion</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getNameConversion()
     * @generated
     * @ordered
     */
    protected String nameConversion = NAME_CONVERSION_EDEFAULT;

    /**
     * The cached value of the '{@link #getLinearConversion() <em>Linear Conversion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getLinearConversion()
     * @generated
     * @ordered
     */
    protected LinearConversionType linearConversion;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected UnitConversionTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ucefPackage.Literals.UNIT_CONVERSION_TYPE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getNameConversion() {
        return nameConversion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setNameConversion(String newNameConversion) {
        String oldNameConversion = nameConversion;
        nameConversion = newNameConversion;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ucefPackage.UNIT_CONVERSION_TYPE__NAME_CONVERSION, oldNameConversion, nameConversion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public LinearConversionType getLinearConversion() {
        return linearConversion;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain basicSetLinearConversion(LinearConversionType newLinearConversion, NotificationChain msgs) {
        LinearConversionType oldLinearConversion = linearConversion;
        linearConversion = newLinearConversion;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION, oldLinearConversion, newLinearConversion);
            if (msgs == null) msgs = notification; else msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setLinearConversion(LinearConversionType newLinearConversion) {
        if (newLinearConversion != linearConversion) {
            NotificationChain msgs = null;
            if (linearConversion != null)
                msgs = ((InternalEObject)linearConversion).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION, null, msgs);
            if (newLinearConversion != null)
                msgs = ((InternalEObject)newLinearConversion).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION, null, msgs);
            msgs = basicSetLinearConversion(newLinearConversion, msgs);
            if (msgs != null) msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION, newLinearConversion, newLinearConversion));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION:
                return basicSetLinearConversion(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case ucefPackage.UNIT_CONVERSION_TYPE__NAME_CONVERSION:
                return getNameConversion();
            case ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION:
                return getLinearConversion();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case ucefPackage.UNIT_CONVERSION_TYPE__NAME_CONVERSION:
                setNameConversion((String)newValue);
                return;
            case ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION:
                setLinearConversion((LinearConversionType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case ucefPackage.UNIT_CONVERSION_TYPE__NAME_CONVERSION:
                setNameConversion(NAME_CONVERSION_EDEFAULT);
                return;
            case ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION:
                setLinearConversion((LinearConversionType)null);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case ucefPackage.UNIT_CONVERSION_TYPE__NAME_CONVERSION:
                return NAME_CONVERSION_EDEFAULT == null ? nameConversion != null : !NAME_CONVERSION_EDEFAULT.equals(nameConversion);
            case ucefPackage.UNIT_CONVERSION_TYPE__LINEAR_CONVERSION:
                return linearConversion != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (nameConversion: ");
        result.append(nameConversion);
        result.append(')');
        return result.toString();
    }

} //UnitConversionTypeImpl
