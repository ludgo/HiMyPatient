module.exports = {
  attributes: {
    first_name: {
      type: 'string',
      size: 32,
      minLength: 3,
      maxLength: 32,
      required: true,
      notNull: true
    },
    last_name: {
      type: 'string',
      size: 32,
      minLength: 3,
      maxLength: 32,
      required: true,
      notNull: true
    },
    birth_date: {
      type: 'date',
      datetime: true,
      required: true,
      notNull: true
    },
    blood_type: {
      type: 'string',
      enum: ['A', 'B', 'AB', '0'],
      required: true,
      notNull: true
    },
    last_glucose: {
      type: 'float',
      required: true,
      notNull: true
    },
    image_url: {
      type: 'string',
      size: 2048,
      defaultsTo: null
    },
    allergens: {
      type: 'array',
      min: 1,
      max: 14,
      maxLength: 14,
      required: true,
      defaultsTo: []
    }
  }
};
