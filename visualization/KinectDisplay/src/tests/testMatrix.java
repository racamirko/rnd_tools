package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jogamp.graph.math.Quaternion;

import data.QuaternionMathematics;
import data.matrix.IllegalDimensionException;
import data.matrix.Matrix;
import data.matrix.MatrixMathematics;

public class testMatrix {

	@Test
	public void testQuatNormalization(){
		// This just shows me that my "normalization" and quaternion "normalization" are NOT the same
		Quaternion inQuat = new Quaternion(1.0f, 2.0f, 3.0f, 2.0f);
		Quaternion outQuat = QuaternionMathematics.normByW(inQuat);
//		System.out.println( "[" + inQuat.getX() + ", " + inQuat.getY() + ", "+inQuat.getZ()+", "+inQuat.getW()+"]" );
		assertEquals(inQuat.getX(), 1.0f, 0.01);
		assertEquals(inQuat.getY(), 2.0f, 0.01);
		assertEquals(inQuat.getZ(), 3.0f, 0.01);
		assertEquals(inQuat.getW(), 2.0f, 0.01);
		
		assertEquals(outQuat.getX(), 0.5f, 0.01);
		assertEquals(outQuat.getY(), 1.0f, 0.01);
		assertEquals(outQuat.getZ(), 1.5f, 0.01);
		assertEquals(outQuat.getW(), 1.0f, 0.01);
	}
	
	@Test
	public void testMultMatIdentity() {
		double bla[][] = {{1.0, 0.0, 0.0, 0.0}, {0.0, 1.0, 0.0, 0.0}, {0.0, 0.0, 1.0, 0.0}, {0.0, 0.0, 0.0, 1.0}};
		Matrix identMat = new Matrix( bla );
		Quaternion inQuat = new Quaternion(1.0f, 2.0f, 3.0f, 1.0f);
		Quaternion outQuat = new Quaternion();
		try {
			outQuat = MatrixMathematics.multiply(identMat, inQuat, true);
		} catch (IllegalDimensionException e) {
			e.printStackTrace();
		}
		assertEquals(inQuat.getX(), outQuat.getX(), 0.01);
		assertEquals(inQuat.getY(), outQuat.getY(), 0.01);
		assertEquals(inQuat.getZ(), outQuat.getZ(), 0.01);
		assertEquals(inQuat.getW(), outQuat.getW(), 0.01);
	}

	@Test
	public void testMultMatTranslation() {
		double bla[][] = {{1.0, 0.0, 0.0, 1.0}, {0.0, 1.0, 0.0, 1.0}, {0.0, 0.0, 1.0, 1.0}, {0.0, 0.0, 0.0, 1.0}};
		Matrix identMat = new Matrix( bla );
		Quaternion inQuat = new Quaternion(1.0f, 2.0f, 3.0f, 1.0f);
		Quaternion outQuat = new Quaternion();
		try {
			outQuat = MatrixMathematics.multiply(identMat, inQuat, true);
		} catch (IllegalDimensionException e) {
			e.printStackTrace();
		}
		assertEquals(inQuat.getX()+1.0f, outQuat.getX(), 0.01);
		assertEquals(inQuat.getY()+1.0f, outQuat.getY(), 0.01);
		assertEquals(inQuat.getZ()+1.0f, outQuat.getZ(), 0.01);
		assertEquals(inQuat.getW(), outQuat.getW(), 0.01);
	}

	@Test
	public void testRotationAndTranslation(){
		double transMatRaw[][] = {{1.0, 2.0, 3.0, 4.0},{5.0, 6.0, 7.0, 8.0},{9.0, 10.0, 11.0, 12.0}, {13.0, 14.0, 15.0, 16.0}};
		Matrix transMat = new Matrix(transMatRaw);
		Quaternion inQuat = new Quaternion(1.0f, 2.0f, 3.0f, 1.0f);
		Quaternion outQuat = new Quaternion();
		try {
			outQuat = MatrixMathematics.multiply(transMat, inQuat, true);
		} catch (IllegalDimensionException e) {
			e.printStackTrace();
		}
		assertEquals(0.1765, outQuat.getX(), 0.01);
		assertEquals(0.4510, outQuat.getY(), 0.01);
		assertEquals(0.7255, outQuat.getZ(), 0.01);
		assertEquals(1.0, outQuat.getW(), 0.01);
	}
	
}
