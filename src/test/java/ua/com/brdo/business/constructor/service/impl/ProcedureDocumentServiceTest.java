package ua.com.brdo.business.constructor.service.impl;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.repository.ProcedureDocumentRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;
import ua.com.brdo.business.constructor.service.impl.ProcedureDocumentServiceImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProcedureDocumentServiceTest {

    private final static Long NON_EXISTENT_ID = 404L;

    @Mock
    private ProcedureDocumentRepository procedureDocumentRepository;

    @InjectMocks
    private ProcedureDocumentServiceImpl serviceTesting;
    private ProcedureDocument dummyProcDoc = new ProcedureDocument();
    private List<ProcedureDocument> dummyProcDocs = Collections.singletonList(dummyProcDoc);

    @Before
    public void setUp() throws Exception{
        dummyProcDoc.setId(20L);

        when(procedureDocumentRepository.exists(dummyProcDoc.getId())).thenReturn(true);
        when(procedureDocumentRepository.exists(NON_EXISTENT_ID)).thenReturn(false);
        when(procedureDocumentRepository.exists((Long) null)).thenThrow(new IllegalArgumentException());
        when(procedureDocumentRepository.findOne(dummyProcDoc.getId())).thenReturn(dummyProcDoc);
        when(procedureDocumentRepository.saveAndFlush(dummyProcDoc)).thenReturn(dummyProcDoc);
        when(procedureDocumentRepository.findAll()).thenReturn(dummyProcDocs);
    }
    @Test
    public void createTest() throws Exception {
        ProcedureDocument resultProcDoc = serviceTesting.create(dummyProcDoc);

        verify(procedureDocumentRepository).saveAndFlush(dummyProcDoc);
        assertEquals(dummyProcDoc, resultProcDoc);
    }

    @Test(expected = NotFoundException.class)
    public void createNullTest() throws Exception {
        serviceTesting.create(null);
    }

    @Test
    public void findAllTest() throws Exception {
        List<ProcedureDocument> foundProcDoc = serviceTesting.getAll();

        verify(procedureDocumentRepository).findAll();
        assertEquals(dummyProcDocs, foundProcDoc);
    }

    @Test
    public void findByIdTest() throws Exception {
        final long id = dummyProcDoc.getId();

        ProcedureDocument actualProcedureDoc = serviceTesting.findById(id);

        assertSame(actualProcedureDoc, dummyProcDoc);
        verify(procedureDocumentRepository).findOne(id);
    }

    @Test(expected = NotFoundException.class)
    public void findByNotExistIdTest() throws Exception {
        serviceTesting.findById(NON_EXISTENT_ID);
    }

    @Test
    public void updateTest() throws Exception {
        serviceTesting.update(dummyProcDoc);

        verify(procedureDocumentRepository).saveAndFlush(dummyProcDoc);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundTest() throws Exception {
        dummyProcDoc.setId(NON_EXISTENT_ID);

        serviceTesting.update(dummyProcDoc);
    }

    @Test(expected = NotFoundException.class)
    public void updateByNullTest() throws Exception {
        serviceTesting.update(null);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        serviceTesting.delete(dummyProcDoc.getId());
        verify(procedureDocumentRepository).delete(dummyProcDoc.getId());
    }
    @Test(expected = NotFoundException.class)
    public void deleteByNotExistingIdTest() throws Exception {
        serviceTesting.delete(NON_EXISTENT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnDeleteIfDocumentHasNoId() throws Exception {
        dummyProcDoc.setId(null);

        serviceTesting.delete(dummyProcDoc);
    }

    @Test
    public void shouldDeleteDocument() throws  Exception{
        serviceTesting.delete(dummyProcDoc);

        verify(procedureDocumentRepository).delete(dummyProcDoc);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnDeleteIfDocumentDoesNotExist() throws Exception {
        dummyProcDoc.setId(NON_EXISTENT_ID);

        serviceTesting.delete(dummyProcDoc);
    }
}
