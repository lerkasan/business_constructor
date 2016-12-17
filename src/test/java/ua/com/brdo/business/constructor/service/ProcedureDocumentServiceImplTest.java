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
import ua.com.brdo.business.constructor.model.ProcedureDocument;
import ua.com.brdo.business.constructor.repository.ProcedureDocumentRepository;
import ua.com.brdo.business.constructor.service.impl.ProcedureDocumentServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class ProcedureDocumentServiceImplTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private ProcedureDocumentRepository procedureDocumentRepository;
    @InjectMocks
    private ProcedureDocumentService serviceTesting = new ProcedureDocumentServiceImpl(procedureDocumentRepository);
    @Mock
    private ProcedureDocument dummyProcedureDocument;

    @Test
    public void shouldCreateTest() throws Exception {
        when(procedureDocumentRepository.saveAndFlush(dummyProcedureDocument)).thenReturn(dummyProcedureDocument);

        ProcedureDocument actualProcedureDocument = serviceTesting.create(dummyProcedureDocument);

        verify(procedureDocumentRepository).saveAndFlush(dummyProcedureDocument);
        assertSame(dummyProcedureDocument, actualProcedureDocument);
    }

    @Test
    public void shouldFindAllTest() throws Exception {
        List<ProcedureDocument> expected = Lists.newArrayList(dummyProcedureDocument, dummyProcedureDocument, dummyProcedureDocument);
        when(procedureDocumentRepository.findAll()).thenReturn(expected);

        List<ProcedureDocument> actual = serviceTesting.getAll();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindByIdTest() throws Exception {
        long anyId = 1;
        when(procedureDocumentRepository.findOne(anyId)).thenReturn(dummyProcedureDocument);

        ProcedureDocument actual = serviceTesting.findById(anyId);

        assertSame(dummyProcedureDocument, actual);
    }

    @Test
    public void shouldUpdateTest() throws Exception {
        long anyId = 2;
        when(dummyProcedureDocument.getId()).thenReturn(anyId);

        when(procedureDocumentRepository.findOne(anyId)).thenReturn(dummyProcedureDocument);

        serviceTesting.update(dummyProcedureDocument);

        verify(procedureDocumentRepository).saveAndFlush(dummyProcedureDocument);
    }

    @Test
    public void shouldDeleteTest() throws Exception {
        long anyId = 1;

        serviceTesting.delete(anyId);

        verify(procedureDocumentRepository).delete(anyId);
    }
}
