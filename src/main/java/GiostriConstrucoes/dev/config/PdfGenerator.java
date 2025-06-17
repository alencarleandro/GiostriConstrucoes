package GiostriConstrucoes.dev.config;

import GiostriConstrucoes.dev.model.Pedido;
import GiostriConstrucoes.dev.model.Produto;
import GiostriConstrucoes.dev.model.Usuario;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class PdfGenerator {

    public byte[] gerarPdfRelatorioVendas(List<Pedido> pedidos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            float[] headerCols = {520f};
            com.itextpdf.layout.element.Table header = new com.itextpdf.layout.element.Table(headerCols);
            header.setBackgroundColor(new DeviceRgb(63, 169, 219));
            header.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));
            header.addCell(new Cell().add(new Paragraph("RELATÓRIO DE VENDAS"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(20f)
                    .setMarginBottom(20f)
                    .setFontSize(24f)
                    .setBorder(Border.NO_BORDER));

            document.add(header);
            document.add(new Paragraph("\n"));

            float[] colWidths = {80f, 160f, 140f, 140f};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(colWidths);

            DeviceRgb headerColor = new DeviceRgb(63, 169, 219);
            DeviceRgb white = new DeviceRgb(255, 255, 255);

            table.addHeaderCell(new Cell().add(new Paragraph("Pedido ID"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Cliente"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Data"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Valor Total"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));

            double valorTotalGeral = 0;

            for (Pedido pedido : pedidos) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(pedido.getId()))));
                table.addCell(new Cell().add(new Paragraph(pedido.getCliente().getNome())));

                // Converter Instant para LocalDateTime e formatar
                Instant dataInstant = pedido.getDataDeLancamento();
                LocalDateTime dataLocal = dataInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
                String dataFormatada = dataLocal.format(formatter);
                table.addCell(new Cell().add(new Paragraph(dataFormatada)));

                table.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", pedido.valorTotal())))
                        .setTextAlignment(TextAlignment.RIGHT));
                valorTotalGeral += pedido.valorTotal();
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            com.itextpdf.layout.element.Table totalTable = new com.itextpdf.layout.element.Table(new float[]{380, 140});
            totalTable.addCell(new Cell().add(new Paragraph("Valor Total"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(white)
                    .setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", valorTotalGeral)))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(headerColor)
                    .setFontColor(white)
                    .setBorder(Border.NO_BORDER));

            document.add(totalTable);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF de vendas", e);
        }
    }

    public byte[] gerarPdfRelatorioEstoque(List<Produto> produtos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            float[] headerCols = {520f};
            com.itextpdf.layout.element.Table header = new com.itextpdf.layout.element.Table(headerCols);
            header.setBackgroundColor(new DeviceRgb(63, 169, 219));
            header.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));
            header.addCell(new Cell().add(new Paragraph("RELATÓRIO DE ESTOQUE"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(20f)
                    .setMarginBottom(20f)
                    .setFontSize(24f)
                    .setBorder(Border.NO_BORDER));

            document.add(header);
            document.add(new Paragraph("\n"));

            float[] colWidths = {100f, 130f, 80f, 50f, 60f, 80f};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(colWidths);

            DeviceRgb headerColor = new DeviceRgb(63, 169, 219);
            DeviceRgb white = new DeviceRgb(255, 255, 255);

            table.addHeaderCell(new Cell().add(new Paragraph("Nome"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Descrição"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Categoria"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Qtd"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Preço Unit."))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Preço Total"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));

            double totalEstoque = 0;

            for (Produto produto : produtos) {
                double precoTotal = produto.getPreco() * produto.getQuantidadeEmEstoque();
                totalEstoque += precoTotal;

                table.addCell(new Cell().add(new Paragraph(produto.getNome())));
                table.addCell(new Cell().add(new Paragraph(produto.getDescricao())));
                table.addCell(new Cell().add(new Paragraph(produto.getCategoria())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(produto.getQuantidadeEmEstoque())))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", produto.getPreco())))
                        .setTextAlignment(TextAlignment.RIGHT));
                table.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", precoTotal)))
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(table);
            document.add(new Paragraph("\n"));

            com.itextpdf.layout.element.Table totalTable = new com.itextpdf.layout.element.Table(new float[]{440, 80});
            totalTable.addCell(new Cell().add(new Paragraph("Valor Total em Estoque"))
                    .setBackgroundColor(headerColor)
                    .setFontColor(white)
                    .setBorder(Border.NO_BORDER));
            totalTable.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", totalEstoque)))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setBackgroundColor(headerColor)
                    .setFontColor(white)
                    .setBorder(Border.NO_BORDER));

            document.add(totalTable);
            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF de estoque", e);
        }
    }

    public byte[] gerarPdfRelatorioUsuarios(List<Usuario> usuarios) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            PdfWriter pdfWriter = new PdfWriter(baos);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);
            pdfDocument.setDefaultPageSize(PageSize.A4);

            float[] headerCols = {520f};
            com.itextpdf.layout.element.Table header = new com.itextpdf.layout.element.Table(headerCols);
            header.setBackgroundColor(new DeviceRgb(63, 169, 219));
            header.setFontColor(Color.convertRgbToCmyk(new DeviceRgb(255, 255, 255)));
            header.addCell(new Cell().add(new Paragraph("RELATÓRIO DE USUÁRIOS"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE)
                    .setMarginTop(20f)
                    .setMarginBottom(20f)
                    .setFontSize(24f)
                    .setBorder(Border.NO_BORDER));

            document.add(header);
            document.add(new Paragraph("\n"));

            float[] colWidths = {250f, 100f, 120f};
            com.itextpdf.layout.element.Table table = new com.itextpdf.layout.element.Table(colWidths);

            DeviceRgb headerColor = new DeviceRgb(63, 169, 219);
            DeviceRgb white = new DeviceRgb(255, 255, 255);

            table.addHeaderCell(new Cell().add(new Paragraph("Nome do Usuário"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Qtd Pedidos"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Gasto"))
                    .setBackgroundColor(headerColor).setFontColor(white).setTextAlignment(TextAlignment.CENTER));

            double totalGeral = 0;

            for (Usuario usuario : usuarios) {
                int qtdPedidos = usuario.getPedidos().size();
                double totalGasto = 0;

                for (Pedido pedido : usuario.getPedidos()) {
                    totalGasto += pedido.valorTotal() + pedido.getValorEntrega();
                }

                totalGeral += totalGasto;

                table.addCell(new Cell().add(new Paragraph(usuario.getNome() + " " + usuario.getSobrenome())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(qtdPedidos)))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph("R$ " + String.format("%.2f", totalGasto)))
                        .setTextAlignment(TextAlignment.RIGHT));
            }

            document.add(table);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF de usuários", e);
        }
    }



}
