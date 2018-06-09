package net.swiftpk.client.terrian;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.swiftpk.client.cache.Archive;
import net.swiftpk.client.cache.Data;
import net.swiftpk.client.cache.Utils;
import net.swiftpk.client.gfx.Surface;
import net.swiftpk.client.loader.various.AppletUtils;
import net.swiftpk.client.scene.GameModel;
import net.swiftpk.client.scene.Scene;
import net.swiftpk.client.util.DataConversions;
import net.swiftpk.client.util.Sector;

public class World {
	public boolean aBoolean591;

	public boolean aBoolean602;

	public GameModel aModel_587;

	public GameModel aModelArray596[];

	public GameModel aModelArrayArray580[][];

	public GameModel aModelArrayArray598[][];

	public final int anInt572 = 96;

	public final int anInt573 = 96;

	public final int anInt574 = 0xbc614e;

	public final int anInt575 = 128;

	public int anInt588;

	public int anIntArray597[];

	public int anIntArrayArray581[][];

	public int anIntArrayArray586[][];

	private int areaX, areaY;

	public Surface gameImage;

	public int modelAdjacency[][];

	public int objectDirs[][] = new int[96][96];

	public boolean playerIsAlive;

	public Scene scene;

	public int selectedX[];

	public int selectedY[];

	public int[][] tiles = null;

	public int walkSteps[][];

	public World(Scene scene, Surface gameImage, boolean debug) {
		if(debug) {
			tiles = new int[1000][4000];
		}
		selectedX = new int[18432];
		walkSteps = new int[96][96];
		aModelArrayArray580 = new GameModel[4][64];
		anIntArrayArray581 = new int[96][96];
		anIntArrayArray586 = new int[4][2304];
		anInt588 = 750;
		aBoolean591 = true;
		selectedY = new int[18432];
		aModelArray596 = new GameModel[64];
		anIntArray597 = new int[256];
		aModelArrayArray598 = new GameModel[4][64];
		modelAdjacency = new int[96][96];
		aBoolean602 = false;
		playerIsAlive = false;
		this.scene = scene;
		this.gameImage = gameImage;
		sectors = new Sector[4];
		try {
			tileArchive = new Archive(Utils.readFile(new File(AppletUtils.CACHE + "/world_landscape.jag")));
		} catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < 64; i++) {
			anIntArray597[i] = Scene.method305(255 - i * 4,
											   255 - (int) (i * 1.75D), 255 - i * 4);
		}

		for(int j = 0; j < 64; j++) {
			anIntArray597[j + 64] = Scene.method305(j * 3, 144, 0);
		}

		for(int k = 0; k < 64; k++) {
			anIntArray597[k + 128] = Scene.method305(192 - (int) (k * 1.5D),
													 144 - (int) (k * 1.5D), 0);
		}

		for(int l = 0; l < 64; l++) {
			anIntArray597[l + 192] = Scene.method305(96 - (int) (l * 1.5D),
													 48 + (int) (l * 1.5D), 0);
		}

	}

	public void addObject(int x, int y, int id, int direction) {
		if(x < 0 || y < 0 || x >= 95 || y >= 95) {
			return;
		}
		if(Data.objectType[id] == 1 || Data.objectType[id] == 2) {
			int width;
			int height;
			if(direction == 0 || direction == 4) {
				width = Data.objectWidth[id];
				height = Data.objectHeight[id];
			} else {
				height = Data.objectWidth[id];
				width = Data.objectHeight[id];
			}
			for(int lX = x; lX < x + width; lX++) {
				for(int lY = y; lY < y + height; lY++) {
					if(Data.objectType[id] == 1) {
						modelAdjacency[lX][lY] |= 0x40;
					} else if(direction == 0) {
						modelAdjacency[lX][lY] |= 2;
						if(lX > 0) {
							orMaskModelAdjacency(lX - 1, lY, 8);
						}
					} else if(direction == 2) {
						modelAdjacency[lX][lY] |= 4;
						if(lY < 95) {
							orMaskModelAdjacency(lX, lY + 1, 1);
						}
					} else if(direction == 4) {
						modelAdjacency[lX][lY] |= 8;
						if(lX < 95) {
							orMaskModelAdjacency(lX + 1, lY, 2);
						}
					} else if(direction == 6) {
						modelAdjacency[lX][lY] |= 1;
						if(lY > 0) {
							orMaskModelAdjacency(lX, lY - 1, 4);
						}
					}
				}

			}

			method407(x, y, width, height);
		}
	}

	public void andMaskModelAdjacency(int i, int j, int k) {
		modelAdjacency[i][j] &= 65535 - k;
	}

	public int getElevation(int i, int j) {

		int k = i >> 7;
		int l = j >> 7;
		int i1 = i & 0x7f;
		int j1 = j & 0x7f;
		if(k < 0 || l < 0 || k >= 95 || l >= 95) {
			return 0;
		}
		int k1;
		int l1;
		int i2;
		if(i1 <= 128 - j1) {
			k1 = method396(k, l);
			l1 = method396(k + 1, l) - k1;
			i2 = method396(k, l + 1) - k1;
		} else {
			k1 = method396(k + 1, l + 1);
			l1 = method396(k, l + 1) - k1;
			i2 = method396(k + 1, l) - k1;
			i1 = 128 - i1;
			j1 = 128 - j1;
		}
		int j2 = k1 + (l1 * i1) / 128 + (i2 * j1) / 128;
		return j2;
	}

	public int getModelAdjacency(int i, int j) {
		if(i < 0 || j < 0 || i >= 96 || j >= 96) {
			return 0;
		} else {
			return modelAdjacency[i][j];
		}
	}

	public int getStepCount(int walkSectionX, int walkSectionY, int x1, int y1,
							int x2, int y2, int walkSectionXArray[], int walkSectionYArray[],
							boolean flag) {
		// System.out.println("walkSection="+walkSectionX+","+walkSectionY+"  1="+x1+","+y1+"  2="+x2+","+y2);
		for(int k1 = 0; k1 < 96; k1++) {
			for(int l1 = 0; l1 < 96; l1++) {
				walkSteps[k1][l1] = 0;
			}

		}

		int i2 = 0;
		int j2 = 0;
		int k2 = walkSectionX;
		int l2 = walkSectionY;
		walkSteps[walkSectionX][walkSectionY] = 99;
		walkSectionXArray[i2] = walkSectionX;
		walkSectionYArray[i2++] = walkSectionY;
		int i3 = walkSectionXArray.length;
		boolean canWalk = false;
		while(j2 != i2) {
			k2 = walkSectionXArray[j2];
			l2 = walkSectionYArray[j2];
			// System.out.println("j2="+j2+",i2="+i2+",k2="+k2+",l2="+l2+",canWalk="+canWalk);
			j2 = (j2 + 1) % i3;
			if(k2 >= x1 && k2 <= x2 && l2 >= y1 && l2 <= y2) {
				// System.out.println("canwalk=true [0]");
				canWalk = true;
				break;
			}
			if(flag) {
				if(k2 > 0 && k2 - 1 >= x1 && k2 - 1 <= x2 && l2 >= y1 &&
						 l2 <= y2 && (modelAdjacency[k2 - 1][l2] & 8) == 0) {
					System.out.println("canwalk=true [1]");
					canWalk = true;
					break;
				}
				if(k2 < 95 && k2 + 1 >= x1 && k2 + 1 <= x2 && l2 >= y1 &&
						 l2 <= y2 && (modelAdjacency[k2 + 1][l2] & 2) == 0) {
					System.out.println("canwalk=true [2]");
					canWalk = true;
					break;
				}
				if(l2 > 0 && k2 >= x1 && k2 <= x2 && l2 - 1 >= y1 &&
						 l2 - 1 <= y2 &&
						 (modelAdjacency[k2][l2 - 1] & 4) == 0) {
					System.out.println("canwalk=true [3]");
					canWalk = true;
					break;
				}
				if(l2 < 95 && k2 >= x1 && k2 <= x2 && l2 + 1 >= y1 &&
						 l2 + 1 <= y2 &&
						 (modelAdjacency[k2][l2 + 1] & 1) == 0) {
					System.out.println("canwalk=true [4]");
					canWalk = true;
					break;
				}
			}
			if(k2 > 0 && walkSteps[k2 - 1][l2] == 0 &&
					 (modelAdjacency[k2 - 1][l2] & 0x78) == 0) {
				walkSectionXArray[i2] = k2 - 1;
				walkSectionYArray[i2] = l2;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 - 1][l2] = 2;
			}
			if(k2 < 95 && walkSteps[k2 + 1][l2] == 0 &&
					 (modelAdjacency[k2 + 1][l2] & 0x72) == 0) {
				walkSectionXArray[i2] = k2 + 1;
				walkSectionYArray[i2] = l2;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 + 1][l2] = 8;
			}
			if(l2 > 0 && walkSteps[k2][l2 - 1] == 0 &&
					 (modelAdjacency[k2][l2 - 1] & 0x74) == 0) {
				walkSectionXArray[i2] = k2;
				walkSectionYArray[i2] = l2 - 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2][l2 - 1] = 1;
			}
			if(l2 < 95 && walkSteps[k2][l2 + 1] == 0 &&
					 (modelAdjacency[k2][l2 + 1] & 0x71) == 0) {
				walkSectionXArray[i2] = k2;
				walkSectionYArray[i2] = l2 + 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2][l2 + 1] = 4;
			}
			if(k2 > 0 && l2 > 0 && (modelAdjacency[k2][l2 - 1] & 0x74) == 0 &&
					 (modelAdjacency[k2 - 1][l2] & 0x78) == 0 &&
					 (modelAdjacency[k2 - 1][l2 - 1] & 0x7c) == 0 &&
					 walkSteps[k2 - 1][l2 - 1] == 0) {
				walkSectionXArray[i2] = k2 - 1;
				walkSectionYArray[i2] = l2 - 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 - 1][l2 - 1] = 3;
			}
			if(k2 < 95 && l2 > 0 && (modelAdjacency[k2][l2 - 1] & 0x74) == 0 &&
					 (modelAdjacency[k2 + 1][l2] & 0x72) == 0 &&
					 (modelAdjacency[k2 + 1][l2 - 1] & 0x76) == 0 &&
					 walkSteps[k2 + 1][l2 - 1] == 0) {
				walkSectionXArray[i2] = k2 + 1;
				walkSectionYArray[i2] = l2 - 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 + 1][l2 - 1] = 9;
			}
			if(k2 > 0 && l2 < 95 && (modelAdjacency[k2][l2 + 1] & 0x71) == 0 &&
					 (modelAdjacency[k2 - 1][l2] & 0x78) == 0 &&
					 (modelAdjacency[k2 - 1][l2 + 1] & 0x79) == 0 &&
					 walkSteps[k2 - 1][l2 + 1] == 0) {
				walkSectionXArray[i2] = k2 - 1;
				walkSectionYArray[i2] = l2 + 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 - 1][l2 + 1] = 6;
			}
			if(k2 < 95 && l2 < 95 && (modelAdjacency[k2][l2 + 1] & 0x71) == 0 &&
					 (modelAdjacency[k2 + 1][l2] & 0x72) == 0 &&
					 (modelAdjacency[k2 + 1][l2 + 1] & 0x73) == 0 &&
					 walkSteps[k2 + 1][l2 + 1] == 0) {
				walkSectionXArray[i2] = k2 + 1;
				walkSectionYArray[i2] = l2 + 1;
				i2 = (i2 + 1) % i3;
				walkSteps[k2 + 1][l2 + 1] = 12;
			}
			// int tmp = wallArray[k2][l2];
			// int x=areaX+k2;
			// int y=areaY+l2;
			// if(x > 230 && x < 240 && y > 160 && y < 170)
			// System.out.println("wallArray["+x+"]["+y+"] = "+tmp);
		}
		if(!canWalk) {
			// System.out.println("canWalk = false, returned -1");
			return -1;
		}
		j2 = 0;
		walkSectionXArray[j2] = k2;
		walkSectionYArray[j2++] = l2;
		int k3;
		for(int j3 = k3 = walkSteps[k2][l2]; k2 != walkSectionX ||
				 l2 != walkSectionY; j3 = walkSteps[k2][l2]) {
			if(j3 != k3) {
				k3 = j3;
				walkSectionXArray[j2] = k2;
				walkSectionYArray[j2++] = l2;
			}
			if((j3 & 2) != 0) {
				k2++;
			} else if((j3 & 8) != 0) {
				k2--;
			}
			if((j3 & 1) != 0) {
				l2++;
			} else if((j3 & 4) != 0) {
				l2--;
			}
		}

		return j2;
	}

	public int method396(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return (sectors[byte0].getTile(i, j).groundElevation & 0xff) * 3;
	}

	public void method400() {
		for(int i = 0; i < 96; i++) {
			for(int j = 0; j < 96; j++) {
				if(method418(i, j, 0) == 250) {
					if(i == 47 && method418(i + 1, j, 0) != 250 &&
							 method418(i + 1, j, 0) != 2) {
						method404(i, j, 9);
					} else if(j == 47 && method418(i, j + 1, 0) != 250 &&
							 method418(i, j + 1, 0) != 2) {
						method404(i, j, 9);
					} else {
						method404(i, j, 2);
					}
				}
			}

		}
	}

	public void method402(int i, int j, int k, int l, int i1) {
		GameModel model = aModelArray596[i + j * 8];
		for(int j1 = 0; j1 < model.verticeCount; j1++) {
			if(model.vertexX[j1] == k * 128 && model.vertexZ[j1] == l * 128) {
				model.setVertexAmbience(j1, i1);
				return;
			}
		}

	}

	public void method403(int i, int j, int k, int l, int i1) {
		int j1 = Data.doorModelVar1[i];
		if(anIntArrayArray581[j][k] < 0x13880) {
			anIntArrayArray581[j][k] += 0x13880 + j1;
		}
		if(anIntArrayArray581[l][i1] < 0x13880) {
			anIntArrayArray581[l][i1] += 0x13880 + j1;
		}
	}

	public void method404(int i, int j, int k) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		sectors[byte0].getTile(i, j).groundOverlay = (byte) k;
	}

	public void method407(int x, int y, int width, int height) {
		if(x < 1 || y < 1 || x + width >= 96 || y + height >= 96) {
			return;
		}
		for(int lX = x; lX <= x + width; lX++) {
			for(int lY = y; lY <= y + height; lY++) {
				if((getModelAdjacency(lX, lY) & 0x63) != 0 ||
						 (getModelAdjacency(lX - 1, lY) & 0x59) != 0 ||
						 (getModelAdjacency(lX, lY - 1) & 0x56) != 0 ||
						 (getModelAdjacency(lX - 1, lY - 1) & 0x6c) != 0) {
					method419(lX, lY, 35);
				} else {
					method419(lX, lY, 0);
				}
			}

		}

	}

	public void method409(int i, int j, int k, boolean flag) {
		int l = (i + 24) / 48;
		int i1 = (j + 24) / 48;
		loadSection(l - 1, i1 - 1, k, 0);
		loadSection(l, i1 - 1, k, 1);
		loadSection(l - 1, i1, k, 2);
		loadSection(l, i1, k, 3);
		method400();
		if(aModel_587 == null) {
			aModel_587 = new GameModel(18688, 18688, true, true, false, false,
									   true);
		}
		if(flag) {
			gameImage.blackScreen();
			for(int j1 = 0; j1 < 96; j1++) {
				for(int l1 = 0; l1 < 96; l1++) {
					modelAdjacency[j1][l1] = 0;
				}

			}

			GameModel model = aModel_587;
			model.clear();
			for(int j2 = 0; j2 < 96; j2++) {
				for(int i3 = 0; i3 < 96; i3++) {
					int i4 = -method396(j2, i3);
					if(method418(j2, i3, k) > 0 &&
							 Data.anIntArray116[method418(j2, i3, k) - 1] == 4) {
						i4 = 0;
					}
					if(method418(j2 - 1, i3, k) > 0 &&
							 Data.anIntArray116[method418(j2 - 1, i3, k) - 1] == 4) {
						i4 = 0;
					}
					if(method418(j2, i3 - 1, k) > 0 &&
							 Data.anIntArray116[method418(j2, i3 - 1, k) - 1] == 4) {
						i4 = 0;
					}
					if(method418(j2 - 1, i3 - 1, k) > 0 &&
							 Data.anIntArray116[method418(j2 - 1, i3 - 1, k) - 1] == 4) {
						i4 = 0;
					}
					int j5 = model.vertexAt(j2 * 128, i4, i3 * 128);
					int j7 = (int) (Math.random() * 10D) - 5;
					model.setVertexAmbience(j5, j7);
				}

			}

			for(int j3 = 0; j3 < 95; j3++) {
				for(int j4 = 0; j4 < 95; j4++) {
					int k5 = method423(j3, j4);
					int k7 = anIntArray597[k5];
					int i10 = k7;
					int k12 = k7;
					int l14 = 0;
					if(k == 1 || k == 2) {
						k7 = 0xbc614e;
						i10 = 0xbc614e;
						k12 = 0xbc614e;
					}
					if(method418(j3, j4, k) > 0) {
						int l16 = method418(j3, j4, k);
						int l5 = Data.anIntArray116[l16 - 1];
						int i19 = method427(j3, j4, k);
						k7 = i10 = Data.anIntArray115[l16 - 1];
						if(l5 == 4) {
							k7 = 1;
							i10 = 1;
							if(l16 == 12) {
								k7 = 31;
								i10 = 31;
							}
						}
						if(l5 == 5) {
							if(method420(j3, j4) > 0 &&
									 method420(j3, j4) < 24000) {
								if(method422(j3 - 1, j4, k, k12) != 0xbc614e &&
										 method422(j3, j4 - 1, k, k12) != 0xbc614e) {
									k7 = method422(j3 - 1, j4, k, k12);
									l14 = 0;
								} else if(method422(j3 + 1, j4, k, k12) != 0xbc614e &&
										 method422(j3, j4 + 1, k, k12) != 0xbc614e) {
									i10 = method422(j3 + 1, j4, k, k12);
									l14 = 0;
								} else if(method422(j3 + 1, j4, k, k12) != 0xbc614e &&
										 method422(j3, j4 - 1, k, k12) != 0xbc614e) {
									i10 = method422(j3 + 1, j4, k, k12);
									l14 = 1;
								} else if(method422(j3 - 1, j4, k, k12) != 0xbc614e &&
										 method422(j3, j4 + 1, k, k12) != 0xbc614e) {
									k7 = method422(j3 - 1, j4, k, k12);
									l14 = 1;
								}
							}
						} else if(l5 != 2 || method420(j3, j4) > 0 &&
								 method420(j3, j4) < 24000) {
							if(method427(j3 - 1, j4, k) != i19 &&
									 method427(j3, j4 - 1, k) != i19) {
								k7 = k12;
								l14 = 0;
							} else if(method427(j3 + 1, j4, k) != i19 &&
									 method427(j3, j4 + 1, k) != i19) {
								i10 = k12;
								l14 = 0;
							} else if(method427(j3 + 1, j4, k) != i19 &&
									 method427(j3, j4 - 1, k) != i19) {
								i10 = k12;
								l14 = 1;
							} else if(method427(j3 - 1, j4, k) != i19 &&
									 method427(j3, j4 + 1, k) != i19) {
								k7 = k12;
								l14 = 1;
							}
						}
						if(Data.anIntArray117[l16 - 1] != 0) {
							// System.out.println("Set tile to 64: "+j3+","+j4);
							modelAdjacency[j3][j4] |= 0x40;
						}
						if(Data.anIntArray116[l16 - 1] == 2) {
							modelAdjacency[j3][j4] |= 0x80;
						}
					}
					method413(j3, j4, l14, k7, i10);
					int i17 = ((method396(j3 + 1, j4 + 1) - method396(j3 + 1,
																	  j4)) + method396(j3, j4 + 1)) - method396(j3, j4);
					if(k7 != i10 || i17 != 0) {
						int ai[] = new int[3];
						int ai7[] = new int[3];
						if(l14 == 0) {
							if(k7 != 0xbc614e) {
								ai[0] = j4 + j3 * 96 + 96;
								ai[1] = j4 + j3 * 96;
								ai[2] = j4 + j3 * 96 + 1;
								int l21 = model.makeFace(3, ai, 0xbc614e, k7);
								selectedX[l21] = j3;
								selectedY[l21] = j4;
								model.faceTag[l21] = 0x30d40 + l21;
							}
							if(i10 != 0xbc614e) {
								ai7[0] = j4 + j3 * 96 + 1;
								ai7[1] = j4 + j3 * 96 + 96 + 1;
								ai7[2] = j4 + j3 * 96 + 96;
								int i22 = model.makeFace(3, ai7, 0xbc614e, i10);
								selectedX[i22] = j3;
								selectedY[i22] = j4;
								model.faceTag[i22] = 0x30d40 + i22;
							}
						} else {
							if(k7 != 0xbc614e) {
								ai[0] = j4 + j3 * 96 + 1;
								ai[1] = j4 + j3 * 96 + 96 + 1;
								ai[2] = j4 + j3 * 96;
								int j22 = model.makeFace(3, ai, 0xbc614e, k7);
								selectedX[j22] = j3;
								selectedY[j22] = j4;
								model.faceTag[j22] = 0x30d40 + j22;
							}
							if(i10 != 0xbc614e) {
								ai7[0] = j4 + j3 * 96 + 96;
								ai7[1] = j4 + j3 * 96;
								ai7[2] = j4 + j3 * 96 + 96 + 1;
								int k22 = model.makeFace(3, ai7, 0xbc614e, i10);
								selectedX[k22] = j3;
								selectedY[k22] = j4;
								model.faceTag[k22] = 0x30d40 + k22;
							}
						}
					} else if(k7 != 0xbc614e) {
						int ai1[] = new int[4];
						ai1[0] = j4 + j3 * 96 + 96;
						ai1[1] = j4 + j3 * 96;
						ai1[2] = j4 + j3 * 96 + 1;
						ai1[3] = j4 + j3 * 96 + 96 + 1;
						int l19 = model.makeFace(4, ai1, 0xbc614e, k7);
						selectedX[l19] = j3;
						selectedY[l19] = j4;
						model.faceTag[l19] = 0x30d40 + l19;
					}
				}

			}

			for(int k4 = 1; k4 < 95; k4++) {
				for(int i6 = 1; i6 < 95; i6++) {
					if(method418(k4, i6, k) > 0 &&
							 Data.anIntArray116[method418(k4, i6, k) - 1] == 4) {
						int l7 = Data.anIntArray115[method418(k4, i6, k) - 1];
						int j10 = model.vertexAt(k4 * 128, -method396(k4, i6),
												 i6 * 128);
						int l12 = model.vertexAt((k4 + 1) * 128,
												 -method396(k4 + 1, i6), i6 * 128);
						int i15 = model.vertexAt((k4 + 1) * 128,
												 -method396(k4 + 1, i6 + 1), (i6 + 1) * 128);
						int j17 = model.vertexAt(k4 * 128,
												 -method396(k4, i6 + 1), (i6 + 1) * 128);
						int ai2[] = { j10, l12, i15, j17 };
						int i20 = model.makeFace(4, ai2, l7, 0xbc614e);
						selectedX[i20] = k4;
						selectedY[i20] = i6;
						model.faceTag[i20] = 0x30d40 + i20;
						method413(k4, i6, 0, l7, l7);
					} else if(method418(k4, i6, k) == 0 ||
							 Data.anIntArray116[method418(k4, i6, k) - 1] != 3) {
						if(method418(k4, i6 + 1, k) > 0 &&
								 Data.anIntArray116[method418(k4, i6 + 1, k) - 1] == 4) {
							int i8 = Data.anIntArray115[method418(k4, i6 + 1, k) - 1];
							int k10 = model.vertexAt(k4 * 128,
													 -method396(k4, i6), i6 * 128);
							int i13 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6), i6 * 128);
							int j15 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6 + 1), (i6 + 1) * 128);
							int k17 = model.vertexAt(k4 * 128,
													 -method396(k4, i6 + 1), (i6 + 1) * 128);
							int ai3[] = { k10, i13, j15, k17 };
							int j20 = model.makeFace(4, ai3, i8, 0xbc614e);
							selectedX[j20] = k4;
							selectedY[j20] = i6;
							model.faceTag[j20] = 0x30d40 + j20;
							method413(k4, i6, 0, i8, i8);
						}
						if(method418(k4, i6 - 1, k) > 0 &&
								 Data.anIntArray116[method418(k4, i6 - 1, k) - 1] == 4) {
							int j8 = Data.anIntArray115[method418(k4, i6 - 1, k) - 1];
							int l10 = model.vertexAt(k4 * 128,
													 -method396(k4, i6), i6 * 128);
							int j13 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6), i6 * 128);
							int k15 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6 + 1), (i6 + 1) * 128);
							int l17 = model.vertexAt(k4 * 128,
													 -method396(k4, i6 + 1), (i6 + 1) * 128);
							int ai4[] = { l10, j13, k15, l17 };
							int k20 = model.makeFace(4, ai4, j8, 0xbc614e);
							selectedX[k20] = k4;
							selectedY[k20] = i6;
							model.faceTag[k20] = 0x30d40 + k20;
							method413(k4, i6, 0, j8, j8);
						}
						if(method418(k4 + 1, i6, k) > 0 &&
								 Data.anIntArray116[method418(k4 + 1, i6, k) - 1] == 4) {
							int k8 = Data.anIntArray115[method418(k4 + 1, i6, k) - 1];
							int i11 = model.vertexAt(k4 * 128,
													 -method396(k4, i6), i6 * 128);
							int k13 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6), i6 * 128);
							int l15 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6 + 1), (i6 + 1) * 128);
							int i18 = model.vertexAt(k4 * 128,
													 -method396(k4, i6 + 1), (i6 + 1) * 128);
							int ai5[] = { i11, k13, l15, i18 };
							int l20 = model.makeFace(4, ai5, k8, 0xbc614e);
							selectedX[l20] = k4;
							selectedY[l20] = i6;
							model.faceTag[l20] = 0x30d40 + l20;
							method413(k4, i6, 0, k8, k8);
						}
						if(method418(k4 - 1, i6, k) > 0 &&
								 Data.anIntArray116[method418(k4 - 1, i6, k) - 1] == 4) {
							int l8 = Data.anIntArray115[method418(k4 - 1, i6, k) - 1];
							int j11 = model.vertexAt(k4 * 128,
													 -method396(k4, i6), i6 * 128);
							int l13 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6), i6 * 128);
							int i16 = model.vertexAt((k4 + 1) * 128,
													 -method396(k4 + 1, i6 + 1), (i6 + 1) * 128);
							int j18 = model.vertexAt(k4 * 128,
													 -method396(k4, i6 + 1), (i6 + 1) * 128);
							int ai6[] = { j11, l13, i16, j18 };
							int i21 = model.makeFace(4, ai6, l8, 0xbc614e);
							selectedX[i21] = k4;
							selectedY[i21] = i6;
							model.faceTag[i21] = 0x30d40 + i21;
							method413(k4, i6, 0, l8, l8);
						}
					}
				}

			}

			model.setLight(true, 40, 48, -50, -10, -50);
			aModelArray596 = aModel_587.split(0, 0, 1536, 1536, 8, 64, 233,
											  false);
			for(int j6 = 0; j6 < 64; j6++) {
				scene.addModel(aModelArray596[j6]);
			}

			for(int i9 = 0; i9 < 96; i9++) {
				for(int k11 = 0; k11 < 96; k11++) {
					anIntArrayArray581[i9][k11] = method396(i9, k11);
				}

			}

		}
		aModel_587.clear();
		int k1 = 0x606060;
		for(int i2 = 0; i2 < 95; i2++) {
			for(int k2 = 0; k2 < 95; k2++) {
				int k3 = method415(i2, k2);
				if(k3 > 0 && (Data.doorUnkownVar[k3 - 1] == 0 || aBoolean602)) {
					method421(aModel_587, k3 - 1, i2, k2, i2 + 1, k2);
					if(flag && Data.doorType[k3 - 1] != 0) {
						modelAdjacency[i2][k2] |= 1;
						if(k2 > 0) {
							orMaskModelAdjacency(i2, k2 - 1, 4);
						}
					}
					if(flag) {
						gameImage.drawLineX(i2 * 3, k2 * 3, 3, k1);
					}
				}
				k3 = method426(i2, k2);
				if(k3 > 0 && (Data.doorUnkownVar[k3 - 1] == 0 || aBoolean602)) {
					method421(aModel_587, k3 - 1, i2, k2, i2, k2 + 1);
					if(flag && Data.doorType[k3 - 1] != 0) {
						modelAdjacency[i2][k2] |= 2;
						if(i2 > 0) {
							orMaskModelAdjacency(i2 - 1, k2, 8);
						}
					}
					if(flag) {
						gameImage.drawLineY(i2 * 3, k2 * 3, 3, k1);
					}
				}
				k3 = method420(i2, k2);
				if(k3 > 0 && k3 < 12000 &&
						 (Data.doorUnkownVar[k3 - 1] == 0 || aBoolean602)) {
					method421(aModel_587, k3 - 1, i2, k2, i2 + 1, k2 + 1);
					if(flag && Data.doorType[k3 - 1] != 0) {
						modelAdjacency[i2][k2] |= 0x20;
					}
					if(flag) {
						gameImage.setPixelColor(i2 * 3, k2 * 3, k1);
						gameImage.setPixelColor(i2 * 3 + 1, k2 * 3 + 1, k1);
						gameImage.setPixelColor(i2 * 3 + 2, k2 * 3 + 2, k1);
					}
				}
				if(k3 > 12000 && k3 < 24000 &&
						 (Data.doorUnkownVar[k3 - 12001] == 0 || aBoolean602)) {
					method421(aModel_587, k3 - 12001, i2 + 1, k2, i2, k2 + 1);
					if(flag && Data.doorType[k3 - 12001] != 0) {
						modelAdjacency[i2][k2] |= 0x10;
					}
					if(flag) {
						gameImage.setPixelColor(i2 * 3 + 2, k2 * 3, k1);
						gameImage.setPixelColor(i2 * 3 + 1, k2 * 3 + 1, k1);
						gameImage.setPixelColor(i2 * 3, k2 * 3 + 2, k1);
					}
				}
			}

		}

		if(flag) {
			gameImage.method228(anInt588 - 1, 0, 0, 285, 285);
		}
		aModel_587.setLight(false, 60, 24, -50, -10, -50);
		aModelArrayArray580[k] = aModel_587.split(0, 0, 1536, 1536, 8, 64, 338,
												  true);
		for(int l2 = 0; l2 < 64; l2++) {
			scene.addModel(aModelArrayArray580[k][l2]);
		}

		for(int l3 = 0; l3 < 95; l3++) {
			for(int l4 = 0; l4 < 95; l4++) {
				int k6 = method415(l3, l4);
				if(k6 > 0) {
					method403(k6 - 1, l3, l4, l3 + 1, l4);
				}
				k6 = method426(l3, l4);
				if(k6 > 0) {
					method403(k6 - 1, l3, l4, l3, l4 + 1);
				}
				k6 = method420(l3, l4);
				if(k6 > 0 && k6 < 12000) {
					method403(k6 - 1, l3, l4, l3 + 1, l4 + 1);
				}
				if(k6 > 12000 && k6 < 24000) {
					method403(k6 - 12001, l3 + 1, l4, l3, l4 + 1);
				}
			}

		}
		for(int i5 = 1; i5 < 95; i5++) {
			for(int l6 = 1; l6 < 95; l6++) {
				int j9 = method410(i5, l6);
				if(j9 > 0) {
					int l11 = i5;
					int i14 = l6;
					int j16 = i5 + 1;
					int k18 = l6;
					int j19 = i5 + 1;
					int j21 = l6 + 1;
					int l22 = i5;
					int j23 = l6 + 1;
					int l23 = 0;
					int j24 = anIntArrayArray581[l11][i14];
					int l24 = anIntArrayArray581[j16][k18];
					int j25 = anIntArrayArray581[j19][j21];
					int l25 = anIntArrayArray581[l22][j23];
					if(j24 > 0x13880) {
						j24 -= 0x13880;
					}
					if(l24 > 0x13880) {
						l24 -= 0x13880;
					}
					if(j25 > 0x13880) {
						j25 -= 0x13880;
					}
					if(l25 > 0x13880) {
						l25 -= 0x13880;
					}
					if(j24 > l23) {
						l23 = j24;
					}
					if(l24 > l23) {
						l23 = l24;
					}
					if(j25 > l23) {
						l23 = j25;
					}
					if(l25 > l23) {
						l23 = l25;
					}
					if(l23 >= 0x13880) {
						l23 -= 0x13880;
					}
					if(j24 < 0x13880) {
						anIntArrayArray581[l11][i14] = l23;
					} else {
						anIntArrayArray581[l11][i14] -= 0x13880;
					}
					if(l24 < 0x13880) {
						anIntArrayArray581[j16][k18] = l23;
					} else {
						anIntArrayArray581[j16][k18] -= 0x13880;
					}
					if(j25 < 0x13880) {
						anIntArrayArray581[j19][j21] = l23;
					} else {
						anIntArrayArray581[j19][j21] -= 0x13880;
					}
					if(l25 < 0x13880) {
						anIntArrayArray581[l22][j23] = l23;
					} else {
						anIntArrayArray581[l22][j23] -= 0x13880;
					}
				}
			}

		}

		aModel_587.clear();
		for(int i7 = 1; i7 < 95; i7++) {
			for(int k9 = 1; k9 < 95; k9++) {
				int i12 = method410(i7, k9);
				if(i12 > 0) {
					int j14 = i7;
					int k16 = k9;
					int l18 = i7 + 1;
					int k19 = k9;
					int k21 = i7 + 1;
					int i23 = k9 + 1;
					int k23 = i7;
					int i24 = k9 + 1;
					int k24 = i7 * 128;
					int i25 = k9 * 128;
					int k25 = k24 + 128;
					int i26 = i25 + 128;
					int j26 = k24;
					int k26 = i25;
					int l26 = k25;
					int i27 = i26;
					int j27 = anIntArrayArray581[j14][k16];
					int k27 = anIntArrayArray581[l18][k19];
					int l27 = anIntArrayArray581[k21][i23];
					int i28 = anIntArrayArray581[k23][i24];
					int j28 = Data.roofHeight[i12 - 1];
					if(method424(j14, k16) && j27 < 0x13880) {
						j27 += j28 + 0x13880;
						anIntArrayArray581[j14][k16] = j27;
					}
					if(method424(l18, k19) && k27 < 0x13880) {
						k27 += j28 + 0x13880;
						anIntArrayArray581[l18][k19] = k27;
					}
					if(method424(k21, i23) && l27 < 0x13880) {
						l27 += j28 + 0x13880;
						anIntArrayArray581[k21][i23] = l27;
					}
					if(method424(k23, i24) && i28 < 0x13880) {
						i28 += j28 + 0x13880;
						anIntArrayArray581[k23][i24] = i28;
					}
					if(j27 >= 0x13880) {
						j27 -= 0x13880;
					}
					if(k27 >= 0x13880) {
						k27 -= 0x13880;
					}
					if(l27 >= 0x13880) {
						l27 -= 0x13880;
					}
					if(i28 >= 0x13880) {
						i28 -= 0x13880;
					}
					byte byte0 = 16;
					if(!method416(j14 - 1, k16)) {
						k24 -= byte0;
					}
					if(!method416(j14 + 1, k16)) {
						k24 += byte0;
					}
					if(!method416(j14, k16 - 1)) {
						i25 -= byte0;
					}
					if(!method416(j14, k16 + 1)) {
						i25 += byte0;
					}
					if(!method416(l18 - 1, k19)) {
						k25 -= byte0;
					}
					if(!method416(l18 + 1, k19)) {
						k25 += byte0;
					}
					if(!method416(l18, k19 - 1)) {
						k26 -= byte0;
					}
					if(!method416(l18, k19 + 1)) {
						k26 += byte0;
					}
					if(!method416(k21 - 1, i23)) {
						l26 -= byte0;
					}
					if(!method416(k21 + 1, i23)) {
						l26 += byte0;
					}
					if(!method416(k21, i23 - 1)) {
						i26 -= byte0;
					}
					if(!method416(k21, i23 + 1)) {
						i26 += byte0;
					}
					if(!method416(k23 - 1, i24)) {
						j26 -= byte0;
					}
					if(!method416(k23 + 1, i24)) {
						j26 += byte0;
					}
					if(!method416(k23, i24 - 1)) {
						i27 -= byte0;
					}
					if(!method416(k23, i24 + 1)) {
						i27 += byte0;
					}
					i12 = Data.roofTexture[i12 - 1];

					j27 = -j27;
					k27 = -k27;
					l27 = -l27;
					i28 = -i28;
					if(method420(i7, k9) > 12000 && method420(i7, k9) < 24000 &&
							 method410(i7 - 1, k9 - 1) == 0) {
						int ai8[] = new int[3];
						ai8[0] = aModel_587.vertexAt(l26, l27, i26);
						ai8[1] = aModel_587.vertexAt(j26, i28, i27);
						ai8[2] = aModel_587.vertexAt(k25, k27, k26);
						aModel_587.makeFace(3, ai8, i12, 0xbc614e);
					} else if(method420(i7, k9) > 12000 &&
							 method420(i7, k9) < 24000 &&
							 method410(i7 + 1, k9 + 1) == 0) {
						int ai9[] = new int[3];
						ai9[0] = aModel_587.vertexAt(k24, j27, i25);
						ai9[1] = aModel_587.vertexAt(k25, k27, k26);
						ai9[2] = aModel_587.vertexAt(j26, i28, i27);
						aModel_587.makeFace(3, ai9, i12, 0xbc614e);
					} else if(method420(i7, k9) > 0 &&
							 method420(i7, k9) < 12000 &&
							 method410(i7 + 1, k9 - 1) == 0) {
						int ai10[] = new int[3];
						ai10[0] = aModel_587.vertexAt(j26, i28, i27);
						ai10[1] = aModel_587.vertexAt(k24, j27, i25);
						ai10[2] = aModel_587.vertexAt(l26, l27, i26);
						aModel_587.makeFace(3, ai10, i12, 0xbc614e);
					} else if(method420(i7, k9) > 0 &&
							 method420(i7, k9) < 12000 &&
							 method410(i7 - 1, k9 + 1) == 0) {
						int ai11[] = new int[3];
						ai11[0] = aModel_587.vertexAt(k25, k27, k26);
						ai11[1] = aModel_587.vertexAt(l26, l27, i26);
						ai11[2] = aModel_587.vertexAt(k24, j27, i25);
						aModel_587.makeFace(3, ai11, i12, 0xbc614e);
					} else if(j27 == k27 && l27 == i28) {
						int ai12[] = new int[4];
						ai12[0] = aModel_587.vertexAt(k24, j27, i25);
						ai12[1] = aModel_587.vertexAt(k25, k27, k26);
						ai12[2] = aModel_587.vertexAt(l26, l27, i26);
						ai12[3] = aModel_587.vertexAt(j26, i28, i27);
						aModel_587.makeFace(4, ai12, i12, 0xbc614e);
					} else if(j27 == i28 && k27 == l27) {
						int ai13[] = new int[4];
						ai13[0] = aModel_587.vertexAt(j26, i28, i27);
						ai13[1] = aModel_587.vertexAt(k24, j27, i25);
						ai13[2] = aModel_587.vertexAt(k25, k27, k26);
						ai13[3] = aModel_587.vertexAt(l26, l27, i26);
						aModel_587.makeFace(4, ai13, i12, 0xbc614e);
					} else {
						boolean flag1 = true;
						if(method410(i7 - 1, k9 - 1) > 0) {
							flag1 = false;
						}
						if(method410(i7 + 1, k9 + 1) > 0) {
							flag1 = false;
						}
						if(!flag1) {
							int ai14[] = new int[3];
							ai14[0] = aModel_587.vertexAt(k25, k27, k26);
							ai14[1] = aModel_587.vertexAt(l26, l27, i26);
							ai14[2] = aModel_587.vertexAt(k24, j27, i25);
							aModel_587.makeFace(3, ai14, i12, 0xbc614e);
							int ai16[] = new int[3];
							ai16[0] = aModel_587.vertexAt(j26, i28, i27);
							ai16[1] = aModel_587.vertexAt(k24, j27, i25);
							ai16[2] = aModel_587.vertexAt(l26, l27, i26);
							aModel_587.makeFace(3, ai16, i12, 0xbc614e);
						} else {
							int ai15[] = new int[3];
							ai15[0] = aModel_587.vertexAt(k24, j27, i25);
							ai15[1] = aModel_587.vertexAt(k25, k27, k26);
							ai15[2] = aModel_587.vertexAt(j26, i28, i27);
							aModel_587.makeFace(3, ai15, i12, 0xbc614e);
							int ai17[] = new int[3];
							ai17[0] = aModel_587.vertexAt(l26, l27, i26);
							ai17[1] = aModel_587.vertexAt(j26, i28, i27);
							ai17[2] = aModel_587.vertexAt(k25, k27, k26);
							aModel_587.makeFace(3, ai17, i12, 0xbc614e);
						}
					}
				}
			}

		}

		aModel_587.setLight(true, 50, 50, -50, -10, -50);
		aModelArrayArray598[k] = aModel_587.split(0, 0, 1536, 1536, 8, 64, 169,
												  true);
		for(int l9 = 0; l9 < 64; l9++) {
			scene.addModel(aModelArrayArray598[k][l9]);
		}

		if(aModelArrayArray598[k][0] == null) {
			throw new RuntimeException("null roof!");
		}
		for(int j12 = 0; j12 < 96; j12++) {
			for(int k14 = 0; k14 < 96; k14++) {
				if(anIntArrayArray581[j12][k14] >= 0x13880) {
					anIntArrayArray581[j12][k14] -= 0x13880;
				}
			}

		}

	}

	public int method410(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).roofTexture;
	}

	public void method413(int i, int j, int k, int l, int i1) {
		int j1 = i * 3;
		int k1 = j * 3;
		int l1 = scene.method302(l);
		int i2 = scene.method302(i1);
		l1 = l1 >> 1 & 0x7f7f7f;
		i2 = i2 >> 1 & 0x7f7f7f;
		if(k == 0) {
			gameImage.drawLineX(j1, k1, 3, l1);
			gameImage.drawLineX(j1, k1 + 1, 2, l1);
			gameImage.drawLineX(j1, k1 + 2, 1, l1);
			gameImage.drawLineX(j1 + 2, k1 + 1, 1, i2);
			gameImage.drawLineX(j1 + 1, k1 + 2, 2, i2);
			return;
		}
		if(k == 1) {
			gameImage.drawLineX(j1, k1, 3, i2);
			gameImage.drawLineX(j1 + 1, k1 + 1, 2, i2);
			gameImage.drawLineX(j1 + 2, k1 + 2, 1, i2);
			gameImage.drawLineX(j1, k1 + 1, 1, l1);
			gameImage.drawLineX(j1, k1 + 2, 2, l1);
		}
	}

	public int method415(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).verticalWall & 0xff;
	}

	public boolean method416(int i, int j) {
		return method410(i, j) > 0 || method410(i - 1, j) > 0 ||
				 method410(i - 1, j - 1) > 0 || method410(i, j - 1) > 0;
	}

	public int method418(int i, int j, int k) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).groundOverlay & 0xff;
	}

	public void method419(int x, int y, int k) {
		int l = x / 12;
		int i1 = y / 12;
		int j1 = (x - 1) / 12;
		int k1 = (y - 1) / 12;
		method402(l, i1, x, y, k);
		if(l != j1) {
			method402(j1, i1, x, y, k);
		}
		if(i1 != k1) {
			method402(l, k1, x, y, k);
		}
		if(l != j1 && i1 != k1) {
			method402(j1, k1, x, y, k);
		}
	}

	public int method420(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).diagonalWalls;
	}

	public void method421(GameModel model, int i, int j, int k, int l, int i1) {
		method419(j, k, 40);
		method419(l, i1, 40);
		int j1 = Data.doorModelVar1[i];
		int k1 = Data.doorModelVar2[i];
		int l1 = Data.doorModelVar3[i];
		int i2 = j * 128;
		int j2 = k * 128;
		int k2 = l * 128;
		int l2 = i1 * 128;
		int i3 = model.vertexAt(i2, -anIntArrayArray581[j][k], j2);
		int j3 = model.vertexAt(i2, -anIntArrayArray581[j][k] - j1, j2);
		int k3 = model.vertexAt(k2, -anIntArrayArray581[l][i1] - j1, l2);
		int l3 = model.vertexAt(k2, -anIntArrayArray581[l][i1], l2);
		int ai[] = { i3, j3, k3, l3 };
		int i4 = model.makeFace(4, ai, k1, l1);
		if(Data.doorUnkownVar[i] == 5) {
			model.faceTag[i4] = 30000 + i;
		} else {
			model.faceTag[i4] = 0;
		}
	}

	public int method422(int i, int j, int k, int l) {
		int i1 = method418(i, j, k);
		if(i1 == 0) {
			return l;
		} else {
			return Data.anIntArray115[i1 - 1];
		}
	}

	public int method423(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).groundTexture & 0xFF;
	}

	public boolean method424(int i, int j) {
		return method410(i, j) > 0 && method410(i - 1, j) > 0 &&
				 method410(i - 1, j - 1) > 0 && method410(i, j - 1) > 0;
	}

	public int method426(int i, int j) {
		if(i < 0 || i >= 96 || j < 0 || j >= 96) {
			return 0;
		}
		byte byte0 = 0;
		if(i >= 48 && j < 48) {
			byte0 = 1;
			i -= 48;
		} else if(i < 48 && j >= 48) {
			byte0 = 2;
			j -= 48;
		} else if(i >= 48 && j >= 48) {
			byte0 = 3;
			i -= 48;
			j -= 48;
		}
		return sectors[byte0].getTile(i, j).horizontalWall & 0xff;
	}

	public int method427(int i, int j, int k) {
		int l = method418(i, j, k);
		if(l == 0) {
			return -1;
		}
		int i1 = Data.anIntArray116[l - 1];
		return i1 != 2 ? 0 : 1;
	}

	public void method428(GameModel models[]) {
		for(int i = 0; i < 94; i++) {
			for(int j = 0; j < 94; j++) {
				if(method420(i, j) > 48000 && method420(i, j) < 60000) {
					int k = method420(i, j) - 48001;
					int l = objectDirs[i][j];// method417(i, j);
					int i1;
					int j1;
					if(l == 0 || l == 4) {
						i1 = Data.objectWidth[k];
						j1 = Data.objectHeight[k];
					} else {
						j1 = Data.objectWidth[k];
						i1 = Data.objectHeight[k];
					}
					addObject(i, j, k, l);
					GameModel model = models[Data.objectModelIndex[k]].copy(
							false, true, false, false);
					int k1 = ((i + i + i1) * 128) / 2;
					int i2 = ((j + j + j1) * 128) / 2;
					model.translate(k1, -getElevation(k1, i2), i2);
					model.orient(0, l * 32, 0);
					scene.addModel(model);
					model.setLight(48, 48, -50, -10, -50);
					if(i1 > 1 || j1 > 1) {
						for(int k2 = i; k2 < i + i1; k2++) {
							for(int l2 = j; l2 < j + j1; l2++) {
								if((k2 > i || l2 > j) &&
										 method420(k2, l2) - 48001 == k) {
									int l1 = k2;
									int j2 = l2;
									byte byte0 = 0;
									if(l1 >= 48 && j2 < 48) {
										byte0 = 1;
										l1 -= 48;
									} else if(l1 < 48 && j2 >= 48) {
										byte0 = 2;
										j2 -= 48;
									} else if(l1 >= 48 && j2 >= 48) {
										byte0 = 3;
										l1 -= 48;
										j2 -= 48;
									}
									anIntArrayArray586[byte0][l1 * 48 + j2] = 0;
								}
							}

						}

					}
				}
			}

		}

	}

	public void orMaskModelAdjacency(int x, int y, int direction) {
		modelAdjacency[x][y] |= direction;
		// System.out.println("wallArray["+i+"]["+j+"] |= "+k+" [new value="+wallArray[i][j]);
	}

	public void populateSection(int i, int j, int k) {
		// System.out.println("i="+i+",j="+j+" area="+areaX+","+areaY);
		removeModels();
		int l = (i + 24) / 48;
		int i1 = (j + 24) / 48;
		method409(i, j, k, true);
		if(k == 0) {
			method409(i, j, 1, false);
			method409(i, j, 2, false);
			loadSection(l - 1, i1 - 1, k, 0);
			loadSection(l, i1 - 1, k, 1);
			loadSection(l - 1, i1, k, 2);
			loadSection(l, i1, k, 3);
			method400();
		}
		if(tiles != null) {
			for(int x = 0; x < 96; x++) {
				System.arraycopy(modelAdjacency[x], 0, tiles[areaX + x], areaY, 96);
			}
		}
	}

	public void registerObjectDir(int x, int y, int dir) {
		if(x < 0 || x >= 96 || y < 0 || y >= 96) {
			return;
		}
		objectDirs[x][y] = dir;
	}

	public void removeModels() {
		if(aBoolean591) {
			scene.cleanupModels();
		}
		for(int i = 0; i < 64; i++) {
			aModelArray596[i] = null;
			for(int j = 0; j < 4; j++) {
				aModelArrayArray580[j][i] = null;
			}

			for(int k = 0; k < 4; k++) {
				aModelArrayArray598[k][i] = null;
			}

		}

		System.gc();
	}

	public void removeObject(int x, int y, int id, int direction) {
		if(x < 0 || y < 0 || x >= 95 || y >= 95) {
			return;
		}
		if(Data.objectType[id] == 1 || Data.objectType[id] == 2) {
			int width;
			int height;
			if(direction == 0 || direction == 4) {
				width = Data.objectWidth[id];
				height = Data.objectHeight[id];
			} else {
				height = Data.objectWidth[id];
				width = Data.objectHeight[id];
			}
			for(int lX = x; lX < x + width; lX++) {
				for(int lY = y; lY < y + height; lY++) {
					if(Data.objectType[id] == 1) {
						modelAdjacency[lX][lY] &= 0xffbf;
					} else if(direction == 0) {
						modelAdjacency[lX][lY] &= 0xfffd;
						if(lX > 0) {
							andMaskModelAdjacency(lX - 1, lY, 8);
						}
					} else if(direction == 2) {
						modelAdjacency[lX][lY] &= 0xfffb;
						if(lY < 95) {
							andMaskModelAdjacency(lX, lY + 1, 1);
						}
					} else if(direction == 4) {
						modelAdjacency[lX][lY] &= 0xfff7;
						if(lX < 95) {
							andMaskModelAdjacency(lX + 1, lY, 2);
						}
					} else if(direction == 6) {
						modelAdjacency[lX][lY] &= 0xfffe;
						if(lY > 0) {
							andMaskModelAdjacency(lX, lY - 1, 4);
						}
					}
				}

			}

			method407(x, y, width, height);
		}
	}

	public void removeWallObject(int x, int y, int dir, int type) {
		if(x < 0 || y < 0 || x >= 95 || y >= 95) {
			return;
		}
		if(Data.doorType[type] == 1) {
			switch(dir) {
				case 0:
					modelAdjacency[x][y] &= 0xfffe;
					if(y > 0) {
						andMaskModelAdjacency(x, y - 1, 4);
					}
					break;
				case 1:
					modelAdjacency[x][y] &= 0xfffd;
					if(x > 0) {
						andMaskModelAdjacency(x - 1, y, 8);
					}
					break;
				case 2:
					modelAdjacency[x][y] &= 0xffef;
					break;
				case 3:
					modelAdjacency[x][y] &= 0xffdf;
					break;
				default:
					break;
			}
			method407(x, y, 1, 1);
		}
	}

	public void set(int x, int y) {
		areaX = x;
		areaY = y;
		// System.out.println("set: "+x+","+y+"");
	}

	public void setModelAdjacency(int i, int j, int k, int l) {
		if(i < 0 || j < 0 || i >= 95 || j >= 95) {
			return;
		}
		if(Data.doorType[l] == 1) {
			switch(k) {
				case 0:
					orMaskModelAdjacency(i, j, 1);
					if(j > 0) {
						orMaskModelAdjacency(i, j - 1, 4);
					}
					break;
				case 1:
					orMaskModelAdjacency(i, j, 2);
					if(i > 0) {
						orMaskModelAdjacency(i - 1, j, 8);
					}
					break;
				case 2:
					orMaskModelAdjacency(i, j, 0x10);
					break;
				case 3:
					orMaskModelAdjacency(i, j, 0x20);
					break;
				default:
					break;
			}
			method407(i, j, 1, 1);
		}
	}

	private Archive tileArchive;
	private Sector[] sectors;

	public void loadSection(int sectionX, int sectionY, int height, int sector) {
		Sector s = null;
		String filename = "h" + height + "x" + sectionX + "y" + sectionY;
		byte[] e = tileArchive.getFile(Archive.getHash(filename));
		if(e == null) {
			s = new Sector();
			if(height == 0 || height == 3) {
				for(int i = 0; i < 2304; i++) {
					s.getTile(i).groundOverlay = (byte) (height == 0 ? -6 :
							 8);
				}
			}
		} else {
			try {
				ByteBuffer data = DataConversions
						.streamToBuffer(Utils.gzDecompress(e));
				s = Sector.unpack(data);
			} catch(IOException ex) {
				Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		sectors[sector] = s;
	}

}
