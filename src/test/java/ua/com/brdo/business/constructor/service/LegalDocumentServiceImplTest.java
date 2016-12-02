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
import ua.com.brdo.business.constructor.model.LegalDocument;
import ua.com.brdo.business.constructor.repository.LegalDocumentRepository;
import ua.com.brdo.business.constructor.service.impl.LegalDocumentServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class LegalDocumentServiceImplTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    @Mock
    private LegalDocumentRepository legalDocumentRepository;
    @InjectMocks
    private LegalDocumentService serviceTesting = new LegalDocumentServiceImpl(legalDocumentRepository);
    @Mock
    private LegalDocument dummyLegalDoc;

    @Test
    public void shouldCreateTest() throws Exception {
        when(legalDocumentRepository.saveAndFlush(dummyLegalDoc)).thenReturn(dummyLegalDoc);

        LegalDocument actualLegalDoc = serviceTesting.create(dummyLegalDoc);

        verify(legalDocumentRepository).saveAndFlush(dummyLegalDoc);
        assertSame(dummyLegalDoc, actualLegalDoc);
    }

    @Test
    public void shouldFindAllTest() throws Exception {
        List<LegalDocument> expected = Lists.newArrayList(dummyLegalDoc, dummyLegalDoc, dummyLegalDoc);
        when(legalDocumentRepository.findAll()).thenReturn(expected);

        List<LegalDocument> actual = serviceTesting.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void shouldFindByIdTest() throws Exception {
        long anyId = 3;
        when(legalDocumentRepository.findOne(anyId)).thenReturn(dummyLegalDoc);

        LegalDocument actual = serviceTesting.findById(anyId);

        assertSame(dummyLegalDoc, actual);
    }

    @Test
    public void shouldUpdateTest() throws Exception {
        long anyId = 1;
        when(dummyLegalDoc.getId()).thenReturn(anyId);
        when(legalDocumentRepository.findOne(anyId)).thenReturn(dummyLegalDoc);

        serviceTesting.update(dummyLegalDoc);

        verify(legalDocumentRepository).saveAndFlush(dummyLegalDoc);
    }

    @Test
    public void shouldDeleteTest() throws Exception {
        long anyId = 1;

        serviceTesting.delete(anyId);

        verify(legalDocumentRepository).delete(anyId);
    }

}
