package web;

import model.ProductCategory;
import org.junit.Test;
import serviceapi.OfferManagementService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.*;

public class ProductCategoryManagementServletTest {

    @Test
    public void testNoAliases() throws ServletException, IOException {

        OfferManagementService offerManagementServiceMock = mock(OfferManagementService.class);
        ProductCategoryManagementServlet servlet = new ProductCategoryManagementServlet(offerManagementServiceMock);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter("categoryName")).thenReturn("Smart TV");

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));

        servlet.doPost(requestMock, responseMock);

        verify(offerManagementServiceMock).addCategory(new ProductCategory("Smart TV"));
    }

    @Test
    public void testWithAliases() throws ServletException, IOException {

        OfferManagementService offerManagementServiceMock = mock(OfferManagementService.class);
        ProductCategoryManagementServlet servlet = new ProductCategoryManagementServlet(offerManagementServiceMock);

        HttpServletRequest requestMock = mock(HttpServletRequest.class);
        when(requestMock.getParameter("categoryName")).thenReturn("Smart TV");
        when(requestMock.getParameter("categoryAliases")).thenReturn("TV, Plasma, LCD TV, LED TV");

        ProductCategory expectedCategoryAdded = new ProductCategory("Smart TV");
        expectedCategoryAdded.setAliases(new HashSet<>(Arrays.asList(new String[] {"TV", "Plasma", "LCD TV", "LED TV"})));

        HttpServletResponse responseMock = mock(HttpServletResponse.class);
        when(responseMock.getWriter()).thenReturn(mock(PrintWriter.class));

        servlet.doPost(requestMock, responseMock);

        verify(offerManagementServiceMock).addCategory(expectedCategoryAdded);
    }



}
