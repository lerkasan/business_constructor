package ua.com.brdo.business.constructor.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LegalDocumentServiceImplTest {

    @Mock
    private LegalDocumentRepository legalDocumentRepository;

    @InjectMocks
    private LegalDocumentServiceImpl serviceTesting;
    private LegalDocument dummyLegalDocument = new LegalDocument();
    private long dummyLegalDocumentId = 12;
    private List<LegalDocument> dummyListLegalDocuments = new LinkedList<LegalDocument>();
    private long notExistId = 404;
    @Before
    public void setUp() throws Exception{
        dummyLegalDocument.setId(dummyLegalDocumentId);

        when(legalDocumentRepository.exists(dummyLegalDocument.getId())).thenReturn(true);
        when(legalDocumentRepository.findOne(dummyLegalDocument.getId())).thenReturn(dummyLegalDocument);
        when(legalDocumentRepository.saveAndFlush(dummyLegalDocument)).thenReturn(dummyLegalDocument);
        when(legalDocumentRepository.findAll()).thenReturn(dummyListLegalDocuments);
    }
    @Test
    public void createTest() throws Exception {
        LegalDocument actualLegalDocument = serviceTesting.create(dummyLegalDocument);

        verify(legalDocumentRepository).saveAndFlush(dummyLegalDocument);
        assertEquals(dummyLegalDocument,actualLegalDocument);
    }
    @Test(expected = NotFoundException.class)
    public void createNullTest() throws Exception {
        LegalDocument actualLegalDocument = serviceTesting.create(null);
    }

    @Test
    public void findAllTest() throws Exception {
        List<LegalDocument> list = serviceTesting.findAll();
        verify(legalDocumentRepository).findAll();
        assertEquals(dummyListLegalDocuments,list);
    }

    @Test
    public void findByIdTest() throws Exception {
        LegalDocument actualLegalDocument = serviceTesting.findById(dummyLegalDocument.getId());
        assertSame(actualLegalDocument,dummyLegalDocument);
        verify(legalDocumentRepository).findOne(dummyLegalDocument.getId());
    }
    @Test(expected = NotFoundException.class)
    public void findByNotExistIdTest() throws Exception {
        serviceTesting.findById(notExistId);
    }

    @Test
    public void updateTest() throws Exception {
        serviceTesting.update(dummyLegalDocument);
        verify(legalDocumentRepository).saveAndFlush(dummyLegalDocument);
    }
    @Test(expected = NotFoundException.class)
    public void updateNotFoundTest() throws Exception {
        dummyLegalDocument.setId(notExistId);
        serviceTesting.update(dummyLegalDocument);
    }
    @Test(expected = NotFoundException.class)
    public void updateByNullTest() throws Exception {
        serviceTesting.update(null);
    }


    @Test
    public void deleteByIdTest() throws Exception {
        serviceTesting.delete(dummyLegalDocument.getId());
        verify(legalDocumentRepository).delete(dummyLegalDocument.getId());
    }
    @Test(expected = NotFoundException.class)
    public void deletByNotExistingIdTest() throws Exception {
        serviceTesting.delete(notExistId);
    }

    @Test
    public void deleteLegalDocumentTest() throws  Exception{
        serviceTesting.delete(dummyLegalDocument);
        verify(legalDocumentRepository).delete(dummyLegalDocument);
    }
    @Test(expected = NotFoundException.class)
    public void deleteByNotExistingLegalDocumentTest() throws Exception {
        dummyLegalDocument.setId(notExistId);
        serviceTesting.delete(dummyLegalDocument);
    }
    @Test(expected = NotFoundException.class)
    public void deleteByNullLegalDocumentTest() throws Exception {
        serviceTesting.delete(null);
    }

}