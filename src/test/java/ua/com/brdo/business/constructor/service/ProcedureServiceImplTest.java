package ua.com.brdo.business.constructor.service;

import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ua.com.brdo.business.constructor.model.Procedure;
import ua.com.brdo.business.constructor.repository.ProcedureRepository;
import ua.com.brdo.business.constructor.service.impl.ProcedureServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProcedureServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private ProcedureRepository procedureRepository;
    @InjectMocks
    private ProcedureService serviceTesting = new ProcedureServiceImpl(procedureRepository);
    @Mock
    private Procedure dummyProcedure;

    @Test
    public void shouldCreateTest() throws Exception {
        when(procedureRepository.saveAndFlush(dummyProcedure)).thenReturn(dummyProcedure);

        Procedure actualProcedure = serviceTesting.create(dummyProcedure);

        verify(procedureRepository).saveAndFlush(dummyProcedure);
        assertSame(dummyProcedure, actualProcedure);
    }

    @Test
    public void shouldFindAllTest() throws Exception {
        List<Procedure> expected = Lists.newArrayList(dummyProcedure, dummyProcedure, dummyProcedure);
        when(procedureRepository.findAll()).thenReturn(expected);

        List<Procedure> actual = serviceTesting.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindByIdTest() throws Exception {
        long anyId = 1;
        when(procedureRepository.findOne(anyId)).thenReturn(dummyProcedure);

        Procedure actual = serviceTesting.findById(anyId);

        assertSame(dummyProcedure, actual);
    }

    @Test
    public void shouldUpdateTest() throws Exception {
        long anyId = 3;
        when(dummyProcedure.getId()).thenReturn(anyId);

        when(procedureRepository.findOne(anyId)).thenReturn(dummyProcedure);

        serviceTesting.update(dummyProcedure);

        verify(procedureRepository).saveAndFlush(dummyProcedure);
    }

    @Test
    public void shouldDeleteTest() throws Exception {
        long anyId = 1;

        serviceTesting.delete(anyId);

        verify(procedureRepository).delete(anyId);
    }
}
