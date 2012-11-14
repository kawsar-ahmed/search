/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.solr.hadoop.tika;

import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.solr.hadoop.SolrReducer;
import org.apache.solr.hadoop.tika.TikaMapper;
import org.junit.Before;
import org.junit.Test;

public class TikaMapperReducerTest {

  MapDriver<LongWritable, Text, Text, MapWritable> mapDriver;
  ReduceDriver<Text, MapWritable, Text, MapWritable> reduceDriver;
  MapReduceDriver<LongWritable, Text, Text, MapWritable, Text, MapWritable> mapReduceDriver;

  @Before
  public void setUp() {
    TikaMapper mapper = new TikaMapper();
    SolrReducer reducer = new SolrReducer();
    mapDriver = MapDriver.newMapDriver(mapper);;
    reduceDriver = ReduceDriver.newReduceDriver(reducer);
    mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
  }

  private static class MyMapWritable extends MapWritable {
    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      for (Map.Entry<Writable, Writable> entry: entrySet()) {
        builder.append(entry.toString());
      }
      return builder.toString();
    }
  }

  @Test
  public void testMapper() {
    mapDriver.withInput(new LongWritable(), new Text(
        "A,B"));
    MyMapWritable map = new MyMapWritable();
    map.put(new Text("f0"), new Text("A"));
    map.put(new Text("f1"), new Text("B"));
    mapDriver.withOutput(new Text("null-0"), map);
    mapDriver.runTest();
  }

//  @Test
//  public void testReducer() {
//    List<IntWritable> values = new ArrayList<IntWritable>();
//    values.add(new IntWritable(1));
//    values.add(new IntWritable(1));
//    reduceDriver.withInput(new Text("6"), values);
//    reduceDriver.withOutput(new Text("6"), new IntWritable(2));
//    reduceDriver.runTest();
//  }

}