package ua.com.brdo.business.constructor.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LegalDocumentServiceImplTest {

    private final static Long NON_EXISTENT_ID = 404L;

    @Mock
    private LegalDocumentRepository legalDocumentRepository;

    @InjectMocks
    private LegalDocumentServiceImpl serviceTesting;
    private LegalDocument dummyDoc = new LegalDocument();
    private List<LegalDocument> dummyDocs = Collections.singletonList(dummyDoc);

    @Before
    public void setUp() throws Exception{
        dummyDoc.setId(12L);

        when(legalDocumentRepository.exists(dummyDoc.getId())).thenReturn(true);
        when(legalDocumentRepository.exists(NON_EXISTENT_ID)).thenReturn(false);
        when(legalDocumentRepository.exists((Long) null)).thenThrow(new IllegalArgumentException());
        when(legalDocumentRepository.findOne(dummyDoc.getId())).thenReturn(dummyDoc);
        when(legalDocumentRepository.saveAndFlush(dummyDoc)).thenReturn(dummyDoc);
        when(legalDocumentRepository.findAll()).thenReturn(dummyDocs);
    }

    @Test
    public void shouldCreateDocumentTest() throws Exception {
        LegalDocument resultDoc = serviceTesting.create(dummyDoc);

        verify(legalDocumentRepository).saveAndFlush(dummyDoc);
        assertEquals(dummyDoc,resultDoc);
    }
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCreateByNullTest() throws Exception {
        serviceTesting.create(null);
    }

    @Test
    public void shouldFindAllDocumentsTest() throws Exception {
        List<LegalDocument> foundDocs = serviceTesting.findAll();

        verify(legalDocumentRepository).findAll();
        assertEquals(dummyDocs,foundDocs);
    }

    @Test
    public void shouldFindDocumentByIdTest() throws Exception {
        final long id = dummyDoc.getId();

        LegalDocument actualLegalDocument = serviceTesting.findById(id);

        assertSame(actualLegalDocument, dummyDoc);
        verify(legalDocumentRepository).findOne(id);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenFindByNotExistingDocumentIdTest() throws Exception {
        serviceTesting.findById(NON_EXISTENT_ID);
    }

    @Test
    public void shouldUpdateDocumentTest() throws Exception {
        serviceTesting.update(dummyDoc);

        verify(legalDocumentRepository).saveAndFlush(dummyDoc);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenUpdateDocumentWithNotExistingIdTest() throws Exception {
        dummyDoc.setId(NON_EXISTENT_ID);

        serviceTesting.update(dummyDoc);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenUpdateByNullTest() throws Exception {
        serviceTesting.update(null);
    }


    @Test
    public void shouldDeleteDocumentByIdTest() throws Exception {
        serviceTesting.delete(dummyDoc.getId());

        verify(legalDocumentRepository).delete(dummyDoc.getId());
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenDeleteDocumentByNotExistingIdTest() throws Exception {
        serviceTesting.delete(NON_EXISTENT_ID);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenDeleteDocumentByNullTest() throws Exception {
        serviceTesting.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenDeleteByDocumentWithoutIdTest() throws Exception {
        dummyDoc.setId(null);

        serviceTesting.delete(dummyDoc);
    }

    @Test
    public void shouldDeleteDocumentTest() throws  Exception{
        serviceTesting.delete(dummyDoc);

        verify(legalDocumentRepository).delete(dummyDoc);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionWhenDeleteByDocumentWithNotExistingIdTest() throws Exception {
        dummyDoc.setId(NON_EXISTENT_ID);

        serviceTesting.delete(dummyDoc);
    }

}