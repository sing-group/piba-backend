package org.sing_group.piba.domain.dao.patient;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.patient.PatientDAO;
import org.sing_group.piba.domain.entities.idspace.IdSpace;
import org.sing_group.piba.domain.entities.patient.Patient;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPatientDAO implements PatientDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Patient> dh;

  public DefaultPatientDAO() {
    super();
  }

  public DefaultPatientDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Patient.class, this.em);
  }

  @Override
  public Patient create(Patient patient) {
    patient.setPatientID(encrypt(patient.getPatientID()));
    return this.dh.persist(patient);
  }

  @Override
  public Stream<Patient> getPatients() {
    return this.dh.list().stream();
  }

  @Override
  public Patient get(String id) {
    return this.dh.get(id)
      .orElseThrow(() -> new IllegalArgumentException("Unknown patient: " + id));
  }

  @Override
  public Patient getPatientBy(String patientID, String idSpace) {
    patientID = encrypt(patientID);
    return this.em
      .createQuery("SELECT p FROM Patient p WHERE p.patientID=:patientID and p.idSpace.id=:idSpace", Patient.class)
      .setParameter("patientID", patientID).setParameter("idSpace", idSpace).getSingleResult();

  }

  @Override
  public Patient edit(Patient patient) {
    patient.setPatientID(encrypt(patient.getPatientID()));
    return this.dh.update(patient);
  }

  @Override
  public void delete(Patient patient) {
    this.dh.remove(patient);
  }

  private String encrypt(String patientID) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] array = md.digest(patientID.getBytes());
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < array.length; ++i) {
        sb.append(
          Integer.toHexString((array[i] & 0xFF) | 0x100)
            .substring(1, 3)
        );
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
      return null;
    }

  }

  @Override
  public Stream<Patient> getPatientsBy(IdSpace idSpace) {
    return this.dh.listBy("idSpace", idSpace).stream();
  }

}
