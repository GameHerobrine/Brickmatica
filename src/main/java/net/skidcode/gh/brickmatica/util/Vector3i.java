package net.skidcode.gh.brickmatica.util;

public class Vector3i {
	public int x, y, z;

	public final static Vector3i ZERO = new Vector3i();

	public Vector3i() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Vector3i(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3i add(int i) {
		return add(i, i, i);
	}

	@SuppressWarnings("hiding")
	public Vector3i add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Vector3i sub(Vector3i vec) {
		return sub(vec.x, vec.y, vec.z);
	}

	@SuppressWarnings("hiding")
	public Vector3i sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	@SuppressWarnings("MethodDoesntCallSuperMethod")
    @Override
	public Vector3i clone() {
		return new Vector3i(this.x, this.y, this.z);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Vector3i o)) {
			return false;
		}

        return this.x == o.x && this.y == o.y && this.z == o.z;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + this.x;
		hash = 71 * hash + this.y;
		hash = 71 * hash + this.z;
		return hash;
	}
}
