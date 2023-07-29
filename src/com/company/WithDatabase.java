package com.company;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


// for searching
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;






//import java.io.File;
import java.io.IOException;
//import java.nio.file.Paths;
import java.sql.*;

public class WithDatabase {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:postgresql://localhost:5432/library";
        String username = "postgres";
        String password = "postgres";

        Connection connection = null;
        IndexWriter indexWriter = null;

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        Directory index = new ByteBuffersDirectory();


        try {
            // Establish PostgreSQL connection
            connection = DriverManager.getConnection(jdbcUrl, username, password);

            // Create a Lucene index directory
            // package problem is not allowing me to create a file
//            Directory indexDirectory = FSDirectory.open(new File(indexDirectoryPath));
            indexWriter = new IndexWriter(index, config);

            // Fetch data from PostgreSQL
            String sqlQuery = "SELECT book_id, title, publication_year FROM books";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            while (resultSet.next()) {
                int id = resultSet.getInt("book_id");
                String title = resultSet.getString("title");
                int year = resultSet.getInt("publication_year");

                // Create a Lucene document
                Document document = new Document();
                document.add(new StringField("book_id", String.valueOf(id), Field.Store.YES));
                document.add(new TextField("title", title, Field.Store.YES));
                document.add(new IntPoint("publication_year", year));

                // the integer field is not stored by default but used for fast searching
                document.add(new StoredField("publication_year", year));
//                System.out.println(document.getFields());
                // Add the document to the index
                indexWriter.addDocument(document);
            }

            // Commit changes
            indexWriter.commit();

            System.out.println("Indexing completed successfully.");

        } catch (SQLException e) {
            e.printStackTrace();

        }catch(IOException e)
        {
            e.printStackTrace();
        }
        finally {
            // Close resources in the finally block
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Now try to search something
        try {
            // Open the index directory
            IndexReader indexReader = DirectoryReader.open(index);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            // Search query string
            String queryString = "pride";

            // Parse the search query
            // simple query to search by the title
            QueryParser queryParser = new QueryParser("title", analyzer);


            Query query = queryParser.parse(queryString);


            // query to search all with the word "pride" and that were published before 1950
            // query = for title
            // query2 =  for publication year
            Query query2 = IntPoint.newRangeQuery("publication_year", 0, 1950);


            /// now create a boolean query
            BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
            booleanQuery.add(query, BooleanClause.Occur.MUST);
            booleanQuery.add(query2, BooleanClause.Occur.MUST);

            BooleanQuery builtBooleanQuery = booleanQuery.build();

            // Perform the search and get the top 10 results
            int hitsPerPage = 5;
//            TopDocs docs = indexSearcher.search(query, hitsPerPage);

            TopDocs docs = indexSearcher.search(builtBooleanQuery, hitsPerPage);

            ScoreDoc[] hits = docs.scoreDocs;

            // Process and display the search results
            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document document = indexSearcher.doc(docId);

                // Retrieve the fields from the document
                String id = document.get("book_id");
                String title = document.get("title");
                String year = document.get("publication_year");
                System.out.println(document.getFields());

//                int publicationYear = Integer.parseInt(document.get("publication_year"));

                // Process the retrieved data as needed
                System.out.println("ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Publication: " + year);


            }

            // Close the index reader
            indexReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
