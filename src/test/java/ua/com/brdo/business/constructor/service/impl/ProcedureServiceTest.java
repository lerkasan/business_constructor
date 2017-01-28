package ua.com.brdo.business.constructor.service.impl;


import org.junit.Before;

import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.repository.ProcedureRepository;
import ua.com.brdo.business.constructor.service.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProcedureServiceTest {

    private final static Long NON_EXISTENT_ID = 404L;

    @Mock
    private ProcedureRepository procedureRepository;

    @InjectMocks
    private ProcedureServiceImpl serviceTesting;
    private Procedure dummyProc = new Procedure();
    private List<Procedure> dummyProcs = Collections.singletonList(dummyProc);

    @Before
    public void setUp() throws Exception{
        dummyProc.setId(12L);

        when(procedureRepository.exists(dummyProc.getId())).thenReturn(true);
        when(procedureRepository.exists(NON_EXISTENT_ID)).thenReturn(false);
        when(procedureRepository.exists((Long) null)).thenThrow(new IllegalArgumentException());
        when(procedureRepository.findOne(dummyProc.getId())).thenReturn(dummyProc);
        when(procedureRepository.saveAndFlush(dummyProc)).thenReturn(dummyProc);
        when(procedureRepository.findAll()).thenReturn(dummyProcs);
    }
    @Test
    public void createTest() throws Exception {
        Procedure resultProc = serviceTesting.create(dummyProc);

        verify(procedureRepository).saveAndFlush(dummyProc);
        assertEquals(dummyProc, resultProc);
    }

    @Test(expected = NotFoundException.class)
    public void createNullTest() throws Exception {
        serviceTesting.create(null);
    }

    @Test
    public void findAllTest() throws Exception {
        List<Procedure> foundProc = serviceTesting.getAll();

        verify(procedureRepository).findAll();
        assertEquals(dummyProcs, foundProc);
    }

    @Test
    public void findByIdTest() throws Exception {
        final long id = dummyProc.getId();

        Procedure actualProcedure = serviceTesting.findById(id);

        assertSame(actualProcedure, dummyProc);
        verify(procedureRepository).findOne(id);
    }

    @Test(expected = NotFoundException.class)
    public void findByNotExistIdTest() throws Exception {
        serviceTesting.findById(NON_EXISTENT_ID);
    }

    @Test
    public void updateTest() throws Exception {
        serviceTesting.update(dummyProc);

        verify(procedureRepository).saveAndFlush(dummyProc);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundTest() throws Exception {
        dummyProc.setId(NON_EXISTENT_ID);

        serviceTesting.update(dummyProc);
    }

    @Test(expected = NotFoundException.class)
    public void updateByNullTest() throws Exception {
        serviceTesting.update(null);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        serviceTesting.delete(dummyProc.getId());
        verify(procedureRepository).delete(dummyProc.getId());
    }
    @Test(expected = NotFoundException.class)
    public void deleteByNotExistingIdTest() throws Exception {
        serviceTesting.delete(NON_EXISTENT_ID);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionOnDeleteIfDocumentHasNoId() throws Exception {
        dummyProc.setId(null);

        serviceTesting.delete(dummyProc);
    }

    @Test
    public void shouldDeleteDocument() throws  Exception{
        serviceTesting.delete(dummyProc);

        verify(procedureRepository).delete(dummyProc);
    }

    @Test(expected = NotFoundException.class)
    public void shouldThrowExceptionOnDeleteIfDocumentDoesNotExist() throws Exception {
        dummyProc.setId(NON_EXISTENT_ID);

        serviceTesting.delete(dummyProc);
    }
}
